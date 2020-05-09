package com.flash3388.frc.ds.ui.section;

import com.flash3388.frc.ds.ui.view.MasterControlView;

import java.util.Arrays;

public class ControlSection extends TabbedPane {

    private enum ViewType implements TabbedPane.ViewType {
        MASTER_CONTROL {
            @Override
            public String displayName() {
                return "Control";
            }

            @Override
            public ViewController createController() {
                return new MasterControlView();
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

    public ControlSection() {
        super(Arrays.asList(ViewType.values()));
    }
}
