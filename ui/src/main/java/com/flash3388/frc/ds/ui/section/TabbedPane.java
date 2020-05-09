package com.flash3388.frc.ds.ui.section;

import com.flash3388.frc.ds.DependencyHolder;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class TabbedPane extends TabPane {

    interface ViewType {
        String displayName();
        ViewController createController(Stage owner, DependencyHolder dependencyHolder);
    }

    public static abstract class ViewController extends AnchorPane {


        protected abstract void startUsing() throws Exception;
        protected abstract void stopUsing();
    }

    private final Map<ViewType, Pair<Tab, ViewController>> mTabMap;
    private final AtomicReference<ViewController> mSelectedController;

    protected TabbedPane(Collection<? extends ViewType> viewTypes, Stage owner, DependencyHolder dependencyHolder) {
        mTabMap = new HashMap<>();

        for (ViewType viewType : viewTypes) {
            ViewController controller = viewType.createController(owner, dependencyHolder);

            Tab tab = new Tab(viewType.displayName());
            tab.setContent(controller);
            tab.setClosable(false);

            getTabs().add(tab);
            mTabMap.put(viewType, new Pair<>(tab, controller));
        }

        getSelectionModel().selectedItemProperty().addListener((obs, o, n)-> {
            onControlChange((ViewController) n.getContent());
        });

        mSelectedController = new AtomicReference<>();

        setControlMode(viewTypes.iterator().next());
        setTabDragPolicy(TabDragPolicy.FIXED);
    }

    public void setControlMode(ViewType viewType) {
        Pair<Tab, ViewController> pair = mTabMap.get(viewType);
        if (pair == null) {
            throw new IllegalArgumentException("ViewType not defined in tabs " + viewType);
        }

        getSelectionModel().select(pair.getKey());

        onControlChange(pair.getValue());
    }

    private void onControlChange(ViewController newViewController) {
        ViewController selectedController = mSelectedController.getAndSet(null);
        if (selectedController != null) {
            selectedController.stopUsing();
        }

        try {
            newViewController.startUsing();
            mSelectedController.set(newViewController);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
