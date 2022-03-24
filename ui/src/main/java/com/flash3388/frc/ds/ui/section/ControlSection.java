package com.flash3388.frc.ds.ui.section;

import com.flash3388.frc.ds.DependencyHolder;
import com.flash3388.frc.ds.ui.view.ConnectionStatusView;
import com.flash3388.frc.ds.ui.view.ControlConfigurationView;
import com.flash3388.frc.ds.ui.view.HidStatusView;
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
                return new MasterControlView(dependencyHolder.getDriverStationControl(),
                        dependencyHolder.getBatteryStatus(), dependencyHolder.getCpuStatus(),
                        dependencyHolder.getImageLoader(), dependencyHolder.getClock());
            }
        },
        CONNECTION_STATUS {
            @Override
            public String displayName() {
                return "Connection";
            }

            @Override
            public ViewController createController(Stage owner, DependencyHolder dependencyHolder) {
                return new ConnectionStatusView(dependencyHolder.getDriverStationControl());
            }
        },
        CONFIGURATION_VIEW {
            @Override
            public String displayName() {
                return "Configuration";
            }

            @Override
            public ViewController createController(Stage owner, DependencyHolder dependencyHolder) {
                return new ControlConfigurationView(dependencyHolder.getDriverStationControl());
            }
        },
        HID_VIEW {
            @Override
            public String displayName() {
                return "HID";
            }

            @Override
            public ViewController createController(Stage owner, DependencyHolder dependencyHolder) {
                return new HidStatusView(dependencyHolder.getHidStatus());
            }
        }
        ;
    }

    public ControlSection(Stage owner, DependencyHolder dependencyHolder) {
        super(Arrays.asList(ViewType.values()), owner, dependencyHolder);
    }
}
