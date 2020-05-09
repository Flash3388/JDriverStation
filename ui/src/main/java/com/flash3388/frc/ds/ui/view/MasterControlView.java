package com.flash3388.frc.ds.ui.view;

import com.flash3388.frc.ds.computer.BatteryStatus;
import com.flash3388.frc.ds.computer.CpuStatus;
import com.flash3388.frc.ds.control.RobotControl;
import com.flash3388.frc.ds.control.RobotControlMode;
import com.flash3388.frc.ds.control.TeamStation;
import com.flash3388.frc.ds.ui.section.TabbedPane;
import com.flash3388.frc.ds.ui.util.NodeHelper;
import com.flash3388.frc.ds.util.ImageLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MasterControlView extends TabbedPane.ViewController {

    private static final String CHARGING_IMAGE_RESOURCE = "charging.png";

    private final BatteryStatus mBatteryStatus;
    private final ImageLoader mImageLoader;

    private final Label mElapsedTimeLabel;
    private final ImageView mBatteryChargingIcon;
    private final ComboBox<TeamStation> mTeamStationComboBox;

    private volatile Image mChargingImage;

    public MasterControlView(RobotControl robotControl, BatteryStatus batteryStatus, CpuStatus cpuStatus, ImageLoader imageLoader) {
        final double TOTAL_WIDTH = 400;

        mBatteryStatus = batteryStatus;
        mImageLoader = imageLoader;

        mElapsedTimeLabel = new Label("0.0");
        mBatteryChargingIcon = new ImageView();
        mTeamStationComboBox = new ComboBox<>();

        HBox root = new HBox();
        root.setSpacing(10.0);

        Separator separator = new Separator();
        separator.setValignment(VPos.CENTER);
        separator.setHalignment(HPos.CENTER);
        separator.setOrientation(Orientation.VERTICAL);
        root.getChildren().addAll(
                createLeftSide(robotControl, TOTAL_WIDTH),
                separator,
                createRightSize(batteryStatus, cpuStatus, TOTAL_WIDTH));

        setPrefWidth(TOTAL_WIDTH);
        getChildren().add(root);

        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
    }

    @Override
    protected void startUsing() throws Exception {
        if (mChargingImage == null) {
            mChargingImage = mImageLoader.loadFromResource(CHARGING_IMAGE_RESOURCE);
            mBatteryChargingIcon.setImage(mBatteryStatus.isChargingValue().get() ? mChargingImage : null);
        }
    }

    @Override
    protected void stopUsing() {

    }

    private Node createLeftSide(RobotControl robotControl, double totalWidth) {
        final double LEFT_WIDTH = totalWidth / 10 * 4;

        ToggleGroup masterControlToggleGroup = new ToggleGroup();
        ToggleButton enable = new ToggleButton("Enable");
        enable.setPrefSize(LEFT_WIDTH / 2, 50);
        enable.setToggleGroup(masterControlToggleGroup);
        enable.setOnAction((e)-> {
            robotControl.setEnabled(true);
        });
        ToggleButton disable = new ToggleButton("Disable");
        disable.setPrefSize(LEFT_WIDTH / 2, 50);
        disable.setToggleGroup(masterControlToggleGroup);
        disable.setSelected(true);
        disable.setOnAction((e)-> {
            robotControl.setEnabled(false);
        });

        HBox masterControlButtons = new HBox();
        masterControlButtons.setSpacing(1.0);
        masterControlButtons.getChildren().addAll(enable, disable);

        VBox controlTypes = new VBox();
        controlTypes.setSpacing(1.0);

        ToggleGroup controlTypeToggleGroup = new ToggleGroup();
        for (RobotControlMode controlMode : RobotControlMode.values()) {
            ToggleButton button = new ToggleButton(controlMode.displayName());
            button.setPrefSize(LEFT_WIDTH, 10);
            button.setToggleGroup(controlTypeToggleGroup);
            button.setUserData(controlMode);

            controlTypes.getChildren().add(button);
        }
        controlTypeToggleGroup.getToggles().get(0).setSelected(true);
        controlTypeToggleGroup.selectedToggleProperty().addListener((obs, o, n)-> {
            if (enable.isSelected()) {
                disable.setSelected(true);
            }

            RobotControlMode controlMode = (RobotControlMode) n.getUserData();
            robotControl.setControlMode(controlMode);
        });

        AnchorPane left = new AnchorPane();
        left.setPadding(new Insets(5.0));
        left.setPrefWidth(LEFT_WIDTH);
        left.getChildren().addAll(controlTypes, masterControlButtons);
        AnchorPane.setBottomAnchor(masterControlButtons, 0.0);
        AnchorPane.setLeftAnchor(masterControlButtons, 5.0);
        AnchorPane.setRightAnchor(masterControlButtons, 5.0);
        AnchorPane.setTopAnchor(controlTypes, 0.0);
        AnchorPane.setLeftAnchor(controlTypes, 5.0);
        AnchorPane.setRightAnchor(controlTypes, 5.0);

        return left;
    }

    private Node createRightSize(BatteryStatus batteryStatus, CpuStatus cpuStatus, double totalWidth) {
        final double RIGHT_WIDTH = totalWidth / 10 * 6;
        final int LABEL_COLUMN = 0;
        final int CONTENT_COLUMN = 1;

        GridPane root = new GridPane();
        root.setHgap(3.0);
        root.setVgap(20.0);
        root.setAlignment(Pos.CENTER);
        root.setGridLinesVisible(false);
        root.setPrefWidth(RIGHT_WIDTH);

        Label elapsedTimeTextLabel = new Label("Elapsed Time:");
        root.add(elapsedTimeTextLabel, LABEL_COLUMN, 0);
        root.add(mElapsedTimeLabel, CONTENT_COLUMN, 0);

        mBatteryChargingIcon.setFitWidth(30.0);
        mBatteryChargingIcon.setFitHeight(30.0);
        batteryStatus.isChargingValue().addListener((obs, o, n)-> {
            mBatteryChargingIcon.setImage(n ? mChargingImage : null);
        });
        ProgressBar batteryLevel = new ProgressBar();
        batteryLevel.progressProperty().bind(batteryStatus.levelValue());

        HBox batteryStatusPane = new HBox();
        batteryStatusPane.setSpacing(1.0);
        batteryStatusPane.setAlignment(Pos.CENTER);
        batteryStatusPane.getChildren().addAll(mBatteryChargingIcon, batteryLevel);

        Label batteryLevelLbl = new Label("PC Battery");

        root.add(batteryLevelLbl, LABEL_COLUMN, 3);
        root.add(batteryStatusPane, CONTENT_COLUMN, 3);

        ProgressBar cpuUsage = new ProgressBar();
        cpuUsage.progressProperty().bind(cpuStatus.usageValue());
        Label cpuUsageLbl = new Label("CPU Usage");

        root.add(cpuUsageLbl, LABEL_COLUMN, 4);
        root.add(cpuUsage, CONTENT_COLUMN, 4);


        Label teamStationLabel = new Label("Team Station");
        teamStationLabel.setPadding(new Insets(2.5, 0.0, 0.0, 0.0));

        mTeamStationComboBox.getItems().addAll(TeamStation.getAll());
        mTeamStationComboBox.getSelectionModel().select(0);
        mTeamStationComboBox.setPrefWidth(RIGHT_WIDTH / 2);

        root.add(teamStationLabel, LABEL_COLUMN, 6);
        root.add(mTeamStationComboBox, CONTENT_COLUMN, 6);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(root);
        AnchorPane.setTopAnchor(root, 5.0);
        AnchorPane.setLeftAnchor(root, 0.0);

        return anchorPane;
    }
}
