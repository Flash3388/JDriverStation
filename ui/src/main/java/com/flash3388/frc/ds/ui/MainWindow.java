package com.flash3388.frc.ds.ui;

import com.flash3388.frc.ds.ui.section.BaseInfoSection;
import com.flash3388.frc.ds.ui.section.ControlSection;
import com.flash3388.frc.ds.ui.section.StatusSection;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainWindow extends AnchorPane {

    private final Stage mOwner;

    private final ControlSection mControlSection;
    private final BaseInfoSection mBaseInfoSection;
    private final StatusSection mStatusSection;

    public MainWindow(Stage owner) {
        mOwner = owner;

        mControlSection = new ControlSection();
        Separator separator1 = new Separator();
        separator1.setOrientation(Orientation.VERTICAL);
        separator1.setValignment(VPos.CENTER);
        separator1.setHalignment(HPos.CENTER);
        mBaseInfoSection = new BaseInfoSection();
        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.VERTICAL);
        separator2.setValignment(VPos.CENTER);
        separator2.setHalignment(HPos.CENTER);
        mStatusSection = new StatusSection();

        BorderPane root = new BorderPane();
        root.setLeft(mControlSection);
        root.setCenter(mBaseInfoSection);
        root.setRight(mStatusSection);

        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);

        getChildren().add(root);
    }
}
