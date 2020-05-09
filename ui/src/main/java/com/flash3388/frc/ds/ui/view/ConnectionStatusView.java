package com.flash3388.frc.ds.ui.view;

import com.castle.time.Time;
import com.flash3388.frc.ds.robot.ConnectionStatus;
import com.flash3388.frc.ds.robot.RobotControl;
import com.flash3388.frc.ds.robot.RobotUsageStatus;
import com.flash3388.frc.ds.ui.section.TabbedPane;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ConnectionStatusView extends TabbedPane.ViewController {

    private final RobotUsageStatus mRobotUsageStatus;
    private final ConnectionStatus mConnectionStatus;

    private final CheckBox mFmsConnected;
    private final CheckBox mRobotConnected;
    private final CheckBox mRadioConnected;

    private final Label mCpuUsage;
    private final Label mRamUsage;
    private final Label mDiskUsage;
    private final Label mCanUtilization;

    public ConnectionStatusView(RobotUsageStatus robotUsageStatus, ConnectionStatus connectionStatus, RobotControl robotControl) {
        final double TOTAL_WIDTH = 350;

        mRobotUsageStatus = robotUsageStatus;
        mConnectionStatus = connectionStatus;

        mFmsConnected = new CheckBox();
        mFmsConnected.setDisable(true);
        mRobotConnected = new CheckBox();
        mRobotConnected.setDisable(true);
        mRadioConnected = new CheckBox();
        mRadioConnected.setDisable(true);

        mFmsConnected.selectedProperty().bind(mConnectionStatus.fmsConnectedProperty());
        mRobotConnected.selectedProperty().bind(mConnectionStatus.robotConnectedProperty());
        mRadioConnected.selectedProperty().bind(mConnectionStatus.radioConnectedProperty());

        mCpuUsage = new Label("0.0");
        mRamUsage = new Label("0.0");
        mDiskUsage = new Label("0.0");
        mCanUtilization = new Label("0.0");

        mCpuUsage.textProperty().bind(mRobotUsageStatus.cpuUsageProperty().asString());
        mRamUsage.textProperty().bind(mRobotUsageStatus.ramUsageProperty().asString());
        mDiskUsage.textProperty().bind(mRobotUsageStatus.diskUsageProperty().asString());
        mCanUtilization.textProperty().bind(mRobotUsageStatus.canUtilizationProperty().asString());

        Button rebootRoborio = new Button("Reboot RoboRIO");
        rebootRoborio.setPrefSize(TOTAL_WIDTH / 2, 20);
        rebootRoborio.setOnAction((e)-> {
            robotControl.rebootRoboRio();
        });

        Button restartCode = new Button("Restart Code");
        restartCode.setPrefSize(TOTAL_WIDTH / 2, 20);
        restartCode.setOnAction((e)-> {
            robotControl.restartCode();
        });

        VBox buttons = new VBox();
        buttons.setSpacing(2.0);
        buttons.getChildren().addAll(rebootRoborio, restartCode);

        Node statusPane = createStatusPane(TOTAL_WIDTH);

        getChildren().addAll(statusPane, buttons);

        AnchorPane.setLeftAnchor(statusPane, 5.0);
        AnchorPane.setTopAnchor(statusPane, 5.0);
        AnchorPane.setRightAnchor(statusPane, 5.0);
        AnchorPane.setBottomAnchor(buttons, 5.0);
        AnchorPane.setLeftAnchor(buttons, 5.0);
    }

    @Override
    protected void startUsing() throws Exception {

    }

    @Override
    protected void stopUsing() {

    }

    @Override
    public void update(Time timePassed) {

    }

    private Node createStatusPane(double totalWidth) {
        GridPane connectionStatusPane = new GridPane();
        connectionStatusPane.setVgap(5.0);
        connectionStatusPane.setHgap(5.0);
        connectionStatusPane.setAlignment(Pos.CENTER_LEFT);
        connectionStatusPane.setGridLinesVisible(false);
        connectionStatusPane.add(mFmsConnected, 0, 1);
        connectionStatusPane.add(new Label("FMS"), 1, 1);
        connectionStatusPane.add(mRobotConnected, 0, 2);
        connectionStatusPane.add(new Label("Robot"), 1, 2);
        connectionStatusPane.add(mRadioConnected, 0, 3);
        connectionStatusPane.add(new Label("Bridge/Radio"), 1, 3);

        GridPane robotStatusPane = new GridPane();
        robotStatusPane.setVgap(5.0);
        robotStatusPane.setHgap(5.0);
        robotStatusPane.setAlignment(Pos.CENTER_RIGHT);
        robotStatusPane.setGridLinesVisible(false);
        robotStatusPane.add(new Label("CPU Usage"), 0, 1);
        robotStatusPane.add(mCpuUsage, 1, 1);
        robotStatusPane.add(new Label("RAM Usage"), 0, 2);
        robotStatusPane.add(mRamUsage, 1, 2);
        robotStatusPane.add(new Label("Disk Usage"), 0, 3);
        robotStatusPane.add(mDiskUsage, 1, 3);
        robotStatusPane.add(new Label("CAN Utilization"), 0, 4);
        robotStatusPane.add(mCanUtilization, 1, 4);

        GridPane statusPane = new GridPane();
        statusPane.setVgap(5.0);
        statusPane.setHgap(totalWidth / 10 * 3);
        statusPane.setAlignment(Pos.TOP_CENTER);
        statusPane.setGridLinesVisible(false);
        statusPane.add(new Label("Network Diagnostics"), 0, 0);
        statusPane.add(connectionStatusPane, 0, 1);
        statusPane.add(new Label("Robot Information"), 1, 0);
        statusPane.add(robotStatusPane, 1, 1);

        return statusPane;
    }
}
