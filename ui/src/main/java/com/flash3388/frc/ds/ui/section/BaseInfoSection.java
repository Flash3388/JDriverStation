package com.flash3388.frc.ds.ui.section;

import com.flash3388.frc.ds.DependencyHolder;
import com.flash3388.frc.ds.robot.ConnectionStatus;
import com.flash3388.frc.ds.robot.RobotControl;
import com.flash3388.frc.ds.robot.RobotControlMode;
import com.flash3388.frc.ds.robot.RobotPowerStatus;
import com.flash3388.frc.ds.ui.util.NodeHelper;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class BaseInfoSection extends AnchorPane {

    private final ProgressBar mVoltageProgress;
    private final Label mVoltageLabel;
    private final Rectangle mCommunicationBox;
    private final Rectangle mCodeBox;
    private final Rectangle mJoystickBox;
    private final Label mStatusTextLabel;

    public BaseInfoSection(Stage owner, DependencyHolder dependencyHolder) {
        final double TOTAL_WIDTH = 200.0;

        ConnectionStatus connectionStatus = dependencyHolder.getConnectionStatus();
        RobotPowerStatus robotPowerStatus = dependencyHolder.getRobotPowerStatus();
        RobotControl robotControl = dependencyHolder.getRobotControl();

        Font teamNumberFont = Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 15);
        Label teamNumberLabel = new Label("Team #");
        teamNumberLabel.setFont(teamNumberFont);
        Label teamNumberValueLabel = new Label("0000");
        teamNumberValueLabel.setFont(teamNumberFont);

        HBox voltageLevelBox = new HBox();
        mVoltageProgress = new ProgressBar(0.0);
        mVoltageProgress.setPrefWidth(50);
        mVoltageProgress.setPrefHeight(65);
        mVoltageProgress.getTransforms().setAll(
                new Translate(0, 55),
                new Rotate(-90, 0, 0)
        );
        voltageLevelBox.getChildren().add(mVoltageProgress);
        mVoltageLabel = new Label("--.--");
        robotPowerStatus.voltageProperty().addListener((obs, o, n)-> {
            setVoltage(n.doubleValue(), robotPowerStatus.maxVoltageProperty().get());
        });

        mCommunicationBox = new Rectangle();
        mCommunicationBox.setWidth(20);
        mCommunicationBox.setHeight(20);
        setRectangle(mCommunicationBox, false);
        connectionStatus.robotConnectedProperty().addListener((obs, o, n)-> {
            setRectangle(mCommunicationBox, n);
            setVoltage(-1, 0);
        });
        Label communicationLabel = new Label("Communication");

        mCodeBox = new Rectangle();
        mCodeBox.setWidth(20);
        mCodeBox.setHeight(20);
        setRectangle(mCodeBox, false);
        Label codeLabel = new Label("Code");

        mJoystickBox = new Rectangle();
        mJoystickBox.setFill(Color.RED);
        mJoystickBox.setWidth(20);
        mJoystickBox.setHeight(20);
        setRectangle(mJoystickBox, false);
        Label joystickLabel = new Label("Joystick");

        GridPane statusPane = new GridPane();
        statusPane.setGridLinesVisible(false);
        statusPane.setAlignment(Pos.CENTER_LEFT);
        statusPane.setHgap(10.0);
        statusPane.setVgap(1.0);
        statusPane.add(mCommunicationBox, 0, 0);
        statusPane.add(communicationLabel, 1, 0);
        statusPane.add(mCodeBox, 0, 1);
        statusPane.add(codeLabel, 1, 1);
        statusPane.add(mJoystickBox, 0, 2);
        statusPane.add(joystickLabel, 1, 2);

        mStatusTextLabel = new Label("----");
        mStatusTextLabel.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 12));
        robotControl.controlModeProperty().addListener((obs, o, n)-> {
            setStatusText(n, robotControl.enabledProperty().get());
        });
        robotControl.enabledProperty().addListener((obs, o, n)-> {
            setStatusText(robotControl.controlModeProperty().getValue(), n);
        });
        setStatusText(robotControl.controlModeProperty().getValue(), robotControl.enabledProperty().get());

        GridPane topPane = new GridPane();
        topPane.setGridLinesVisible(false);
        topPane.setPrefWidth(TOTAL_WIDTH);
        topPane.setAlignment(Pos.CENTER);
        topPane.setHgap(50.0);
        topPane.setVgap(30.0);
        topPane.add(teamNumberLabel, 0, 0);
        topPane.add(teamNumberValueLabel, 1, 0);
        topPane.add(voltageLevelBox, 0, 1);
        topPane.add(mVoltageLabel, 1, 1);
        topPane.add(statusPane, 0, 2, 2, 1);
        topPane.add(mStatusTextLabel, 0, 3, 2, 1);

        FlowPane root = new FlowPane();
        root.setPrefWidth(TOTAL_WIDTH);
        root.setVgap(10.0);
        root.setOrientation(Orientation.VERTICAL);
        root.getChildren().addAll(topPane);

        setPadding(new Insets(10.0, 0.0, 10.0, 0.0));
        getChildren().add(root);
        NodeHelper.stretchAnchorChild(root);
    }

    private void setRectangle(Rectangle rectangle, boolean value) {
        rectangle.setFill(value ? Color.GREEN : Color.RED);
    }

    private void setVoltage(double voltage, double maxVoltage) {
        if (voltage < 0) {
            mVoltageLabel.setText(String.format("%02.2f", voltage));
            mVoltageProgress.setProgress(voltage / maxVoltage);
        } else {
            mVoltageLabel.setText("--.--");
            mVoltageProgress.setProgress(0.0);
        }
    }

    private void setStatusText(RobotControlMode controlMode, boolean isEnabled) {
        mStatusTextLabel.setText(String.format("%s %s",
                controlMode.displayName(),
                isEnabled ? "Enabled" : "Disabled"));
    }
}
