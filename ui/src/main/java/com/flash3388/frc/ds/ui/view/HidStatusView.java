package com.flash3388.frc.ds.ui.view;

import com.castle.time.Time;
import com.flash3388.frc.ds.computer.hid.HidStatus;
import com.flash3388.frc.ds.computer.hid.Joystick;
import com.flash3388.frc.ds.ui.controls.HatView;
import com.flash3388.frc.ds.ui.section.TabbedPane;
import com.flash3388.frc.ds.ui.util.NodeHelper;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class HidStatusView extends TabbedPane.ViewController {

    private final HidStatus mHidStatus;
    private final ListView<Container> mListView;

    private final FlowPane mAxisViewsPane;
    private final FlowPane mButtonsViewsPane;
    private final FlowPane mHatsViewsPane;
    private final List<JoystickControlView> mViews;

    public HidStatusView(HidStatus hidStatus) {
        mHidStatus = hidStatus;

        ListView<Container> listView = new ListView<>();
        mListView = listView;
        listView.setCellFactory(param -> new JoystickCell(hidStatus));

        mAxisViewsPane = new FlowPane();
        mAxisViewsPane.setVgap(2);
        mAxisViewsPane.setHgap(2);
        mAxisViewsPane.setOrientation(Orientation.VERTICAL);
        mButtonsViewsPane = new FlowPane();
        mButtonsViewsPane.setVgap(2);
        mButtonsViewsPane.setHgap(2);
        mButtonsViewsPane.setOrientation(Orientation.VERTICAL);
        mHatsViewsPane = new FlowPane();
        mHatsViewsPane.setVgap(2);
        mHatsViewsPane.setHgap(2);
        mHatsViewsPane.setOrientation(Orientation.VERTICAL);

        HBox viewsSubRoot = new HBox();
        viewsSubRoot.getChildren().addAll(mAxisViewsPane, mButtonsViewsPane);
        viewsSubRoot.setSpacing(2);
        viewsSubRoot.setMinHeight(180);

        VBox viewsRoot = new VBox();
        viewsRoot.getChildren().addAll(viewsSubRoot, mHatsViewsPane);

        mViews = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            listView.getItems().add(new Container());
        }

        listView.getSelectionModel().selectedItemProperty().addListener((obs, o, container)-> {
            mViews.clear();
            mAxisViewsPane.getChildren().clear();
            mButtonsViewsPane.getChildren().clear();
            mHatsViewsPane.getChildren().clear();

            if (container == null || container.empty()) {
                return;
            }

            Joystick joystick = container.data;

            for (int i = 0; i < joystick.getAxisCount(); i++) {
                JoystickControlView view = new AxisView(joystick, i);
                mViews.add(view);
                mAxisViewsPane.getChildren().add(view);
            }
            for (int i = 0; i < joystick.getButtonCount(); i++) {
                JoystickControlView view = new ButtonView(joystick, i);
                mViews.add(view);
                mButtonsViewsPane.getChildren().add(view);
            }
            for (int i = 0; i < joystick.getHatCount(); i++) {
                JoystickControlView view = new HatView(joystick, i);
                mViews.add(view);
                mHatsViewsPane.getChildren().add(view);
            }
        });

        hidStatus.addJoystickConnectionListener((event)-> {
            Platform.runLater(()-> {
                int index = event.getJoystick().indexProperty().get();
                Container container = listView.getItems().get(index);

                if (event.isConnected()) {
                    container.data = event.getJoystick();
                } else {
                    if (index == listView.getSelectionModel().getSelectedIndex()) {
                        mViews.clear();
                        mAxisViewsPane.getChildren().clear();
                        mButtonsViewsPane.getChildren().clear();
                        mHatsViewsPane.getChildren().clear();

                        listView.getSelectionModel().clearSelection();
                    }

                    container.data = null;
                }
            });
            listView.refresh();
        });

        HBox root = new HBox();
        root.getChildren().addAll(listView, viewsRoot);
        root.setSpacing(2);
        getChildren().add(root);
        NodeHelper.stretchAnchorChild(root);
    }

    @Override
    protected void startUsing() throws Exception {
        for (Joystick joystick : mHidStatus.getJoysticks()) {
            mListView.getItems().get(joystick.indexProperty().get()).data = joystick;
        }
        mListView.refresh();
    }

    @Override
    protected void stopUsing() {

    }

    @Override
    public void update(Time timePassed) {
        mViews.forEach(JoystickControlView::update);
    }

    private static class Container {
        Joystick data;

        boolean empty() {
            return data == null;
        }
    }

    private static class JoystickCell extends ListCell<Container> {

        public JoystickCell(HidStatus hidStatus) {
            JoystickCell thisCell = this;

            setOnDragDetected(event -> {
                if (getItem() == null || getItem().empty()) {
                    return;
                }

                Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                int index = getListView().getItems().indexOf(getItem());
                content.putString(String.valueOf(index));
                dragboard.setContent(content);

                event.consume();
            });

            setOnDragOver(event -> {
                if (event.getGestureSource() != thisCell &&
                        event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }

                event.consume();
            });

            setOnDragEntered(event -> {
                if (event.getGestureSource() != thisCell &&
                        event.getDragboard().hasString()) {
                    setOpacity(0.3);
                }
            });

            setOnDragExited(event -> {
                if (event.getGestureSource() != thisCell &&
                        event.getDragboard().hasString()) {
                    setOpacity(1);
                }
            });

            setOnDragDropped(event -> {
                if (getItem() == null) {
                    return;
                }

                Dragboard db = event.getDragboard();
                boolean success = false;

                if (db.hasString()) {
                    ObservableList<Container> items = getListView().getItems();

                    int draggedIdx = Integer.parseInt(db.getString());
                    int thisIdx = items.indexOf(getItem());

                    Joystick dragged = items.get(draggedIdx).data;
                    items.get(draggedIdx).data = getItem().data;
                    getItem().data = dragged;

                    hidStatus.reassignJoystickIndex(draggedIdx, thisIdx);

                    getListView().refresh();

                    success = true;
                }
                event.setDropCompleted(success);

                event.consume();
            });

            setOnDragDone(DragEvent::consume);
        }

        @Override
        protected void updateItem(Container item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null || item.empty()) {
                setText("-----");
            } else {
                int index = getListView().getItems().indexOf(item);
                setText(String.format("%d. %s", index, item.data.getName()));
            }
        }
    }

    private static abstract class JoystickControlView extends AnchorPane {

        public abstract void update();
    }

    private static class AxisView extends JoystickControlView {

        private final Joystick mJoystick;
        private final int mIndex;
        private final ProgressBar mBar;

        private AxisView(Joystick joystick, int index) {
            mJoystick = joystick;
            mIndex = index;

            mBar = new ProgressBar();
            mBar.setProgress(0.5);
            mBar.setPrefSize(60, 25);

            getChildren().add(mBar);
            NodeHelper.stretchAnchorChild(mBar);
        }

        @Override
        public void update() {
            double value = mJoystick.getAxisValue(mIndex);
            value = (value + 1.0) / 2.0;
            if (value < 0) {
                value = 0.0;
            }
            mBar.setProgress(value);
        }
    }

    private static class ButtonView extends JoystickControlView {

        private final Joystick mJoystick;
        private final int mIndex;
        private final Rectangle mBar;

        private ButtonView(Joystick joystick, int index) {
            mJoystick = joystick;
            mIndex = index;

            mBar = new Rectangle();
            mBar.setFill(Color.RED);
            mBar.setWidth(30);
            mBar.setHeight(15);

            getChildren().add(mBar);
            NodeHelper.stretchAnchorChild(mBar);
        }

        @Override
        public void update() {
            boolean value = mJoystick.getButtonValue(mIndex);
            mBar.setFill(value ? Color.GREENYELLOW : Color.RED);
        }
    }

    private static class HatView extends JoystickControlView {

        private final Joystick mJoystick;
        private final int mIndex;
        private final com.flash3388.frc.ds.ui.controls.HatView mBar;

        private HatView(Joystick joystick, int index) {
            mJoystick = joystick;
            mIndex = index;

            mBar = new com.flash3388.frc.ds.ui.controls.HatView();
            mBar.setPrefSize(60, 60);

            getChildren().add(mBar);
            NodeHelper.stretchAnchorChild(mBar);
        }

        @Override
        public void update() {
            int value = mJoystick.getHatValue(mIndex);
            mBar.setAngle(value);
        }
    }
}
