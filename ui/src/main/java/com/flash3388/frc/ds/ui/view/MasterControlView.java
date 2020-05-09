package com.flash3388.frc.ds.ui.view;

import com.flash3388.frc.ds.control.RobotControlMode;
import com.flash3388.frc.ds.control.TeamStation;
import com.flash3388.frc.ds.ui.section.TabbedPane;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MasterControlView extends TabbedPane.ViewController {

    private final Label mElapsedTimeLabel;
    private final ComboBox<TeamStation> mTeamStationComboBox;

    public MasterControlView() {
        final double TOTAL_WIDTH = 400;
        final double LEFT_WIDTH = TOTAL_WIDTH / 2;
        final double RIGHT_WIDTH = TOTAL_WIDTH / 2;

        ToggleGroup masterControlToggleGroup = new ToggleGroup();
        ToggleButton enable = new ToggleButton("Enable");
        enable.setPrefSize(LEFT_WIDTH / 2, 50);
        enable.setToggleGroup(masterControlToggleGroup);
        enable.setOnAction((e)-> {

        });
        ToggleButton disable = new ToggleButton("Disable");
        disable.setPrefSize(LEFT_WIDTH / 2, 50);
        disable.setToggleGroup(masterControlToggleGroup);
        disable.setSelected(true);
        disable.setOnAction((e)-> {

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

        AnchorPane elapsedTimePane = new AnchorPane();
        mElapsedTimeLabel = new Label("0.0");
        Label elapsedTimeTextLabel = new Label("Elapsed Time:");
        elapsedTimePane.getChildren().addAll(elapsedTimeTextLabel, mElapsedTimeLabel);
        AnchorPane.setLeftAnchor(elapsedTimeTextLabel, 5.0);
        AnchorPane.setRightAnchor(mElapsedTimeLabel, 5.0);

        AnchorPane teamStationComboBoxPane = new AnchorPane();
        Label teamStationLabel = new Label("Team Station");
        teamStationLabel.setPadding(new Insets(2.5, 0.0, 0.0, 0.0));
        mTeamStationComboBox = new ComboBox<>();
        mTeamStationComboBox.getItems().addAll(TeamStation.getAll());
        mTeamStationComboBox.getSelectionModel().select(0);
        mTeamStationComboBox.setPrefWidth(RIGHT_WIDTH / 2);
        teamStationComboBoxPane.getChildren().addAll(teamStationLabel, mTeamStationComboBox);
        AnchorPane.setLeftAnchor(teamStationLabel, 5.0);
        AnchorPane.setRightAnchor(mTeamStationComboBox, 5.0);

        AnchorPane right = new AnchorPane();
        right.setPrefWidth(RIGHT_WIDTH);
        right.getChildren().addAll(elapsedTimePane, teamStationComboBoxPane);
        AnchorPane.setTopAnchor(elapsedTimePane, 5.0);
        AnchorPane.setLeftAnchor(elapsedTimePane, 0.0);
        AnchorPane.setRightAnchor(elapsedTimePane, 0.0);
        AnchorPane.setBottomAnchor(teamStationComboBoxPane, 5.0);
        AnchorPane.setLeftAnchor(teamStationComboBoxPane, 0.0);
        AnchorPane.setRightAnchor(teamStationComboBoxPane, 0.0);

        HBox root = new HBox();
        root.setSpacing(10.0);

        Separator separator = new Separator();
        separator.setValignment(VPos.CENTER);
        separator.setHalignment(HPos.CENTER);
        separator.setOrientation(Orientation.VERTICAL);
        root.getChildren().addAll(left, separator, right);

        setPrefWidth(TOTAL_WIDTH);
        getChildren().add(root);

        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
    }

    @Override
    protected void startUsing() {

    }

    @Override
    protected void stopUsing() {

    }
}
