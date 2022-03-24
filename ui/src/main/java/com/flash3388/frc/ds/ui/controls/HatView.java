package com.flash3388.frc.ds.ui.controls;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Map;

public class HatView extends GridPane {

    private final Map<Integer, Circle> mAngleToCircle;

    public HatView() {
        setHgap(2);
        setVgap(2);

        mAngleToCircle = new HashMap<>();

        Circle circle;
        circle = createCircle(0, 1);
        mAngleToCircle.put(0, circle);
        circle = createCircle(0, 2);
        mAngleToCircle.put(45, circle);
        circle = createCircle(0, 0);
        mAngleToCircle.put(315, circle);

        circle = createCircle(1, 1);
        mAngleToCircle.put(-1, circle);
        circle = createCircle(1, 2);
        mAngleToCircle.put(90, circle);
        circle = createCircle(1, 0);
        mAngleToCircle.put(270, circle);

        circle = createCircle(2, 1);
        mAngleToCircle.put(180, circle);
        circle = createCircle(2, 2);
        mAngleToCircle.put(135, circle);
        circle = createCircle(2, 0);
        mAngleToCircle.put(225, circle);
    }

    public void setAngle(int angle) {
        Circle selectedCircle = mAngleToCircle.get(angle);
        if (selectedCircle == null) {
            throw new IllegalArgumentException("Bad angle: " + angle);
        }

        for (Circle circle : mAngleToCircle.values()) {
            if (circle.equals(selectedCircle)) {
                continue;
            }

            circle.setFill(Color.RED);
        }

        selectedCircle.setFill(Color.GREENYELLOW);
    }

    private Circle createCircle(int row, int col) {
        Circle circle = new Circle();
        circle.setFill(Color.RED);
        circle.setCenterX(0);
        circle.setCenterY(0);
        circle.setRadius(7);

        add(circle, col, row);
        return circle;
    }
}
