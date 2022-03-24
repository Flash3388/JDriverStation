package com.flash3388.frc.ds.ui.view;

import com.castle.time.Clock;
import com.castle.time.Time;
import com.flash3388.frc.ds.computer.BatteryStatus;
import com.flash3388.frc.ds.computer.CpuStatus;
import com.flash3388.frc.ds.robot.DriverStationControl;
import com.flash3388.frc.ds.robot.RobotControlMode;
import com.flash3388.frc.ds.robot.TeamStation;
import com.flash3388.frc.ds.ui.section.TabbedPane;
import com.flash3388.frc.ds.util.ImageLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.concurrent.TimeUnit;

public class MasterControlView extends TabbedPane.ViewController {

    private static final String CHARGING_IMAGE_RESOURCE = "charging.png";

    private final DriverStationControl mDriverStationControl;
    private final BatteryStatus mBatteryStatus;
    private final ImageLoader mImageLoader;
    private final Clock mClock;

    private final Button mEnable;
    private final Button mDisable;
    private final Label mElapsedTimeLabel;
    private final ImageView mBatteryChargingIcon;
    private final ProgressBar mBatteryLevel;
    private final ProgressBar mCpuUsage;
    private final ComboBox<TeamStation> mTeamStationComboBox;

    private volatile Time mStartEnabledTimestamp;
    private volatile Image mChargingImage;
    private volatile boolean mIsDisabled;

    public MasterControlView(DriverStationControl driverStationControl,
                             BatteryStatus batteryStatus, CpuStatus cpuStatus,
                             ImageLoader imageLoader, Clock clock) {
        final double TOTAL_WIDTH = 350;

        mDriverStationControl = driverStationControl;
        mBatteryStatus = batteryStatus;
        mImageLoader = imageLoader;
        mClock = clock;

        mEnable = new Button("Enable");
        mDisable = new Button("Disable");
        mElapsedTimeLabel = new Label("00:00.0");
        mBatteryChargingIcon = new ImageView();
        mBatteryLevel = new ProgressBar();
        mCpuUsage = new ProgressBar();
        mTeamStationComboBox = new ComboBox<>();
        mIsDisabled = true;

        mBatteryStatus.isChargingProperty().addListener((obs, o, n)-> {
            setIsCharging(n);
        });
        mBatteryLevel.progressProperty().bind(mBatteryStatus.levelProperty());
        mCpuUsage.progressProperty().bind(cpuStatus.usageProperty());

        HBox root = new HBox();
        root.setSpacing(10.0);

        Separator separator = new Separator();
        separator.setValignment(VPos.CENTER);
        separator.setHalignment(HPos.CENTER);
        separator.setOrientation(Orientation.VERTICAL);
        root.getChildren().addAll(
                createLeftSide(driverStationControl, TOTAL_WIDTH),
                separator,
                createRightSize(TOTAL_WIDTH));

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
        }

        setIsCharging(mBatteryStatus.isChargingProperty().get());
    }

    @Override
    protected void stopUsing() {
        disableRun();
    }

    @Override
    public void update(Time timePassed) {
        if (!mIsDisabled && mStartEnabledTimestamp != null) {
            Time delta = mClock.currentTime().sub(mStartEnabledTimestamp).toUnit(TimeUnit.MILLISECONDS);

            Time deltaAsSeconds = delta.toUnit(TimeUnit.SECONDS);
            Time deltaAsMinutes = delta.toUnit(TimeUnit.MINUTES);

            long deltaAsMilliseconds = delta.value() - deltaAsSeconds.toUnit(TimeUnit.MILLISECONDS).value();

            mElapsedTimeLabel.setText(String.format("%02d:%02d:%01d",
                    deltaAsMinutes.value(),
                    deltaAsSeconds.value() - deltaAsMinutes.toUnit(TimeUnit.SECONDS).value(),
                    deltaAsMilliseconds / 100));
        }
    }

    private void setIsCharging(boolean isCharging) {
        if (isCharging) {
            mBatteryChargingIcon.setImage(mChargingImage);
        } else {
            mBatteryChargingIcon.setImage(null);
        }
    }

    private void disableRun() {
        mDriverStationControl.setEnabled(false);
    }

    private Node createLeftSide(DriverStationControl driverStationControl, double totalWidth) {
        final double LEFT_WIDTH = totalWidth / 12 * 5;
        final Font CONTROL_BUTTON_FONT = Font.font(13);
        final Font CONTROL_MODE_FONT = Font.font(11);

        mEnable.setFont(CONTROL_BUTTON_FONT);
        mEnable.setTextFill(Color.GREEN);
        mEnable.setPrefSize(LEFT_WIDTH / 2, 50);
        mEnable.setOnAction((e)-> {
            if (driverStationControl.canEnableRobot()) {
                driverStationControl.setEnabled(true);
            }
        });
        mDisable.setFont(CONTROL_BUTTON_FONT);
        mDisable.setTextFill(Color.RED);
        mDisable.setPrefSize(LEFT_WIDTH / 2, 50);
        mDisable.setDisable(true);
        mDisable.setOnAction((e)-> {
            driverStationControl.setEnabled(false);
        });

        driverStationControl.enabledProperty().addListener((obs, o, n)-> {
            if (!o && n) {
                mStartEnabledTimestamp = mClock.currentTime();
                mEnable.setDisable(true);
                mDisable.setDisable(false);
                mIsDisabled = false;
            } else if (!n && o) {
                mElapsedTimeLabel.setText("00:00.0");
                mStartEnabledTimestamp = null;
                mEnable.setDisable(false);
                mDisable.setDisable(true);
                mIsDisabled = true;
            }
        });

        HBox masterControlButtons = new HBox();
        masterControlButtons.setSpacing(1.0);
        masterControlButtons.getChildren().addAll(mEnable, mDisable);

        VBox controlTypes = new VBox();
        controlTypes.setSpacing(1.0);

        ToggleGroup controlTypeToggleGroup = new ToggleGroup();
        for (RobotControlMode controlMode : RobotControlMode.values()) {
            ToggleButton button = new ToggleButton(controlMode.displayName());
            button.setFont(CONTROL_MODE_FONT);
            button.setPrefSize(LEFT_WIDTH, 10);
            button.setToggleGroup(controlTypeToggleGroup);
            button.setUserData(controlMode);

            controlTypes.getChildren().add(button);
        }
        controlTypeToggleGroup.getToggles().get(0).setSelected(true);
        controlTypeToggleGroup.selectedToggleProperty().addListener((obs, o, n)-> {
            disableRun();

            RobotControlMode controlMode = (RobotControlMode) n.getUserData();
            driverStationControl.setControlMode(controlMode.toDsControlMode());
        });

        AnchorPane left = new AnchorPane();
        left.setPadding(new Insets(4.0));
        left.setPrefWidth(LEFT_WIDTH);
        left.getChildren().addAll(controlTypes, masterControlButtons);
        AnchorPane.setBottomAnchor(masterControlButtons, 0.0);
        AnchorPane.setLeftAnchor(masterControlButtons, 1.0);
        AnchorPane.setRightAnchor(masterControlButtons, 1.0);
        AnchorPane.setTopAnchor(controlTypes, 0.0);
        AnchorPane.setLeftAnchor(controlTypes, 5.0);
        AnchorPane.setRightAnchor(controlTypes, 5.0);

        return left;
    }

    private Node createRightSize(double totalWidth) {
        final double RIGHT_WIDTH = totalWidth / 12 * 7;
        final int LABEL_COLUMN = 0;
        final int CONTENT_COLUMN = 1;

        GridPane root = new GridPane();
        root.setHgap(1.0);
        root.setVgap(20.0);
        root.setAlignment(Pos.CENTER);
        root.setGridLinesVisible(false);
        root.setPrefWidth(RIGHT_WIDTH);

        HBox elapsedTimePane = new HBox();
        elapsedTimePane.setSpacing(1.0);
        elapsedTimePane.setAlignment(Pos.CENTER);
        elapsedTimePane.getChildren().addAll(mElapsedTimeLabel);

        Label elapsedTimeTextLabel = new Label("Elapsed Time");
        elapsedTimeTextLabel.setPrefWidth(RIGHT_WIDTH / 10 * 6);
        root.add(elapsedTimeTextLabel, LABEL_COLUMN, 0);
        root.add(elapsedTimePane, CONTENT_COLUMN, 0);

        GridPane computerStatusLblPane = new GridPane();
        computerStatusLblPane.setHgap(1.0);
        computerStatusLblPane.setVgap(1.0);
        computerStatusLblPane.setAlignment(Pos.CENTER_LEFT);
        computerStatusLblPane.setGridLinesVisible(false);
        computerStatusLblPane.setPrefWidth(RIGHT_WIDTH / 2);

        Label batteryLevelLbl = new Label("PC Battery");
        Label cpuUsageLbl = new Label("CPU Usage");
        computerStatusLblPane.add(batteryLevelLbl, 0, 0);
        computerStatusLblPane.add(cpuUsageLbl, 0, 1);

        GridPane computerStatusDataPane = new GridPane();
        computerStatusDataPane.setHgap(1.0);
        computerStatusDataPane.setVgap(1.0);
        computerStatusDataPane.setAlignment(Pos.CENTER_RIGHT);
        computerStatusDataPane.setGridLinesVisible(false);
        computerStatusDataPane.setPrefWidth(RIGHT_WIDTH / 2);

        mBatteryChargingIcon.setFitWidth(15.0);
        mBatteryChargingIcon.setFitHeight(15.0);

        HBox batteryStatusPane = new HBox();
        batteryStatusPane.setSpacing(1.0);
        batteryStatusPane.setAlignment(Pos.CENTER_RIGHT);
        batteryStatusPane.getChildren().addAll(mBatteryChargingIcon, mBatteryLevel);

        computerStatusDataPane.add(batteryStatusPane, 0, 0);
        computerStatusDataPane.add(mCpuUsage, 0, 1);

        root.add(computerStatusLblPane, LABEL_COLUMN, 3);
        root.add(computerStatusDataPane, CONTENT_COLUMN, 3);

        Label teamStationLabel = new Label("Team Station");

        mTeamStationComboBox.getItems().addAll(TeamStation.values());
        mTeamStationComboBox.getSelectionModel().select(0);
        mTeamStationComboBox.setPrefWidth(RIGHT_WIDTH / 2);
        mDriverStationControl.teamStationProperty().addListener((obs, o, n)-> {
            mTeamStationComboBox.getSelectionModel().select(n);
        });

        root.add(teamStationLabel, LABEL_COLUMN, 8);
        root.add(mTeamStationComboBox, CONTENT_COLUMN, 8);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(root);
        AnchorPane.setTopAnchor(root, 5.0);
        AnchorPane.setLeftAnchor(root, 0.0);

        return anchorPane;
    }
}
