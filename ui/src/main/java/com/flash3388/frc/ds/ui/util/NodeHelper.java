package com.flash3388.frc.ds.ui.util;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class NodeHelper {

    private NodeHelper() {}

    public static void stretchAnchorChild(Node child) {
        AnchorPane.setTopAnchor(child, 0.0);
        AnchorPane.setBottomAnchor(child, 0.0);
        AnchorPane.setRightAnchor(child, 0.0);
        AnchorPane.setLeftAnchor(child, 0.0);
    }
}
