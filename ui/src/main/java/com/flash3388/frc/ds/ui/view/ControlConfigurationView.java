package com.flash3388.frc.ds.ui.view;

import com.castle.time.Time;
import com.flash3388.frc.ds.dashboard.DashboardType;
import com.flash3388.frc.ds.robot.CommunicationProtocol;
import com.flash3388.frc.ds.ui.controls.NumericField;
import com.flash3388.frc.ds.ui.section.TabbedPane;
import com.flash3388.frc.ds.ui.util.NodeHelper;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ControlConfigurationView extends TabbedPane.ViewController {

    public ControlConfigurationView() {
        final double TOTAL_WIDTH = 350;
        final Font TITLE_LABEL_FONT = Font.font(Font.getDefault().getFamily(),
                FontWeight.BOLD, Font.getDefault().getSize());

        Node left = createLeftSide(TOTAL_WIDTH, TITLE_LABEL_FONT);
        Node right = createRightSide(TOTAL_WIDTH, TITLE_LABEL_FONT);

        FlowPane root = new FlowPane();
        root.setPadding(new Insets(5.0));
        root.setPrefWidth(TOTAL_WIDTH);
        root.setOrientation(Orientation.VERTICAL);
        root.setHgap(20.0);
        root.getChildren().addAll(left, right);

        getChildren().add(root);
        NodeHelper.stretchAnchorChild(root);
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

    private Node createLeftSide(double totalWidth, Font titleLabelFont) {
        Label teamNumberLabel = new Label("Team Number");
        teamNumberLabel.setFont(titleLabelFont);
        NumericField teamNumberField = new NumericField(int.class);
        //teamNumberField.valueProperty().bindBidirectional();

        Label dashboardTypeLabel = new Label("Dashboard Type");
        dashboardTypeLabel.setFont(titleLabelFont);
        ComboBox<DashboardType> dashboardTypeComboBox = new ComboBox<>();
        dashboardTypeComboBox.getItems().addAll(DashboardType.values());

        Label protocolLabel = new Label("Protocol");
        protocolLabel.setFont(titleLabelFont);
        ComboBox<CommunicationProtocol> protocolComboBox = new ComboBox<>();
        protocolComboBox.getItems().addAll(CommunicationProtocol.values());

        GridPane left = new GridPane();
        left.setGridLinesVisible(false);
        left.setVgap(5.0);
        left.setHgap(1.0);
        left.setPrefWidth(totalWidth / 2);
        left.add(teamNumberLabel, 0, 0);
        left.add(teamNumberField, 0, 1);
        left.add(dashboardTypeLabel, 0, 2);
        left.add(dashboardTypeComboBox, 0, 3);
        left.add(protocolLabel, 0, 25);
        left.add(protocolComboBox, 0, 26);

        return left;
    }

    private Node createRightSide(double totalWidth, Font titleLabelFont) {
        final double labelWidth = totalWidth / 2 / 5 * 3;
        final double fieldWidth = totalWidth / 2 / 5 * 2;

        Label practiceTiming = new Label("Practice Timing");
        practiceTiming.setFont(titleLabelFont);
        practiceTiming.setPrefWidth(totalWidth / 2);

        Label countdownLabel = new Label("Countdown");
        countdownLabel.setPrefWidth(labelWidth);
        NumericField countdown = new NumericField(int.class);
        countdown.setPrefWidth(fieldWidth);
        Label autonomousLabel = new Label("Autonomous");
        autonomousLabel.setPrefWidth(labelWidth);
        NumericField autonomous = new NumericField(int.class);
        autonomous.setPrefWidth(fieldWidth);
        Label delayLabel = new Label("Delay");
        delayLabel.setPrefWidth(labelWidth);
        NumericField delay = new NumericField(int.class);
        delay.setPrefWidth(fieldWidth);
        Label teleoperatedLabel = new Label("Teleoperated");
        teleoperatedLabel.setPrefWidth(labelWidth);
        NumericField teleoperated = new NumericField(int.class);
        teleoperated.setPrefWidth(fieldWidth);
        Label endgameLabel = new Label("Endgame");
        endgameLabel.setPrefWidth(labelWidth);
        NumericField endgame = new NumericField(int.class);
        endgame.setPrefWidth(fieldWidth);

        GridPane right = new GridPane();
        right.setGridLinesVisible(false);
        right.setVgap(5.0);
        right.setHgap(1.0);
        right.setPrefWidth(totalWidth / 2);
        right.add(practiceTiming, 0, 0, 3, 1);
        right.add(countdownLabel, 0, 1);
        right.add(countdown, 2, 1);
        right.add(autonomousLabel, 0, 2);
        right.add(autonomous, 2, 2);
        right.add(delayLabel, 0, 3);
        right.add(delay, 2, 3);
        right.add(teleoperatedLabel, 0, 4);
        right.add(teleoperated, 2, 4);
        right.add(endgameLabel, 0, 5);
        right.add(endgame, 2, 5);

        return right;
    }
}
