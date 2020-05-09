package com.flash3388.frc.ds.ui.section;

import com.flash3388.frc.ds.ui.controls.GeneralStatusIndicator;
import com.flash3388.frc.ds.ui.controls.TeamInfoIndicator;
import com.flash3388.frc.ds.ui.controls.VoltageIndicator;
import javafx.scene.layout.VBox;

public class BaseInfoSection extends VBox {

    private final TeamInfoIndicator mTeamInfoIndicator;
    private final VoltageIndicator mVoltageIndicator;
    private final GeneralStatusIndicator mGeneralStatusIndicator;

    public BaseInfoSection() {
        mTeamInfoIndicator = new TeamInfoIndicator();
        mVoltageIndicator = new VoltageIndicator();
        mGeneralStatusIndicator = new GeneralStatusIndicator();

        getChildren().addAll(mTeamInfoIndicator, mVoltageIndicator, mGeneralStatusIndicator);
    }
}
