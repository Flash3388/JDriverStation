package com.flash3388.frc.ds.ui.view;

import com.castle.time.Clock;
import com.castle.time.Time;
import com.flash3388.frc.ds.computer.BatteryStatus;
import com.flash3388.frc.ds.computer.CpuStatus;
import com.flash3388.frc.ds.robot.RobotControl;
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

    private final BatteryStatus mBatteryStatus;
    private final ImageLoader mImageLoader;
    private final Clock mClock;

    private final ToggleButton mDisable;
    private final Label mElapsedTimeLabel;
    private final ImageView mBatteryChargingIcon;
    private final ProgressBar mBatteryLevel;
    private final ProgressBar mCpuUsage;
    private final ComboBox<TeamStation> mTeamStationComboBox;

    private Time mStartEnabledTimestamp;
    private volatile Image mChargingImage;

    public MasterControlView(RobotControl robotControl, BatteryStatus batteryStatus, CpuStatus cpuStatus, ImageLoader imageLoader, Clock clock) {
        final double TOTAL_WIDTH = 350;

        mBatteryStatus = batteryStatus;
        mImageLoader = imageLoader;
        mClock = clock;

        mDisable = new ToggleButton("Disable");
        mElapsedTimeLabel = new Label("00:00.0");
        mBatteryChargingIcon = new ImageView();
        mBatteryLevel = new ProgressBar();
        mCpuUsage = new ProgressBar();
        mTeamStationComboBox = new ComboBox<>();

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
                createLeftSide(robotControl, TOTAL_WIDTH),
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
        if (!mDisable.isSelected()) {
            mDisable.setSelected(true);
        }
    }

    @Override
    public void update(Time timePassed) {
        if (!mDisable.isSelected() && mStartEnabledTimestamp != null) {
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

    private Node createLeftSide(RobotControl robotControl, double totalWidth) {
        final double LEFT_WIDTH = totalWidth / 12 * 5;
        final Font CONTROL_BUTTON_FONT = Font.font(13);
        final Font CONTROL_MODE_FONT = Font.font(11);

        ToggleGroup masterControlToggleGroup = new ToggleGroup();
        ToggleButton enable = new ToggleButton("Enable");
        enable.setFont(CONTROL_BUTTON_FONT);
        enable.setTextFill(Color.GREEN);
        enable.setPrefSize(LEFT_WIDTH / 2, 50);
        enable.setToggleGroup(masterControlToggleGroup);
        enable.setOnAction((e)-> {
            mStartEnabledTimestamp = mClock.currentTime();
            robotControl.setEnabled(true);
        });
        mDisable.setFont(CONTROL_BUTTON_FONT);
        mDisable.setTextFill(Color.RED);
        mDisable.setPrefSize(LEFT_WIDTH / 2, 50);
        mDisable.setToggleGroup(masterControlToggleGroup);
        mDisable.setSelected(true);
        mDisable.setOnAction((e)-> {
            robotControl.setEnabled(false);
            mElapsedTimeLabel.setText("00:00.0");
            mStartEnabledTimestamp = null;
        });

        HBox masterControlButtons = new HBox();
        masterControlButtons.setSpacing(1.0);
        masterControlButtons.getChildren().addAll(enable, mDisable);

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
            if (enable.isSelected()) {
                mDisable.setSelected(true);
            }

            RobotControlMode controlMode = (RobotControlMode) n.getUserData();
            robotControl.setControlMode(controlMode);
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

        mTeamStationComboBox.getItems().addAll(TeamStation.getAll());
        mTeamStationComboBox.getSelectionModel().select(0);
        mTeamStationComboBox.setPrefWidth(RIGHT_WIDTH / 2);

        root.add(teamStationLabel, LABEL_COLUMN, 8);
        root.add(mTeamStationComboBox, CONTENT_COLUMN, 8);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(root);
        AnchorPane.setTopAnchor(root, 5.0);
        AnchorPane.setLeftAnchor(root, 0.0);

        return anchorPane;
    }
}
