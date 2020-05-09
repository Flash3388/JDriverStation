package com.flash3388.frc.ds.ui.section;

import com.flash3388.frc.ds.DependencyHolder;
import com.flash3388.frc.ds.ui.view.MasterControlView;
import javafx.stage.Stage;

import java.util.Arrays;

public class ControlSection extends TabbedPane {

    private enum ViewType implements TabbedPane.ViewType {
        MASTER_CONTROL {
            @Override
            public String displayName() {
                return "Control";
            }

            @Override
            public ViewController createController(Stage owner, DependencyHolder dependencyHolder) {
                return new MasterControlView(dependencyHolder.getRobotControl(),
                        dependencyHolder.getBatteryStatus(), dependencyHolder.getCpuStatus(),
                        dependencyHolder.getImageLoader());
            }
        }/*,
        CONNECTION_STATUS {
            @Override
            public String displayName() {
                return null;
            }

            @Override
            public ViewController createController() {
                return null;
            }
        }*/
        ;
    }

    public ControlSection(Stage owner, DependencyHolder dependencyHolder) {
        super(Arrays.asList(ViewType.values()), owner, dependencyHolder);
    }
}
