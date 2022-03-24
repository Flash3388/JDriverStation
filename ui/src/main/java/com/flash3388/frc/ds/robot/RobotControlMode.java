package com.flash3388.frc.ds.robot;

import com.flash3388.frc.ds.api.DsControlMode;

public enum RobotControlMode {
    TELEOPERATED {
        @Override
        public String displayName() {
            return "Operator Control";
        }

        @Override
        public DsControlMode toDsControlMode() {
            return DsControlMode.TELEOPERATED;
        }
    },
    AUTONOMOUS {
        @Override
        public String displayName() {
            return "Autonomous";
        }

        @Override
        public DsControlMode toDsControlMode() {
            return DsControlMode.AUTONOMOUS;
        }
    },
    TEST {
        @Override
        public String displayName() {
            return "Test";
        }

        @Override
        public DsControlMode toDsControlMode() {
            return DsControlMode.TEST;
        }
    }
    ;

    public abstract String displayName();
    public abstract DsControlMode toDsControlMode();

    public static RobotControlMode fromDsControlMode(DsControlMode dsControlMode) {
        for (RobotControlMode robotControlMode : values()) {
            if (robotControlMode.toDsControlMode() == dsControlMode) {
                return robotControlMode;
            }
        }

        throw new EnumConstantNotPresentException(RobotControlMode.class, dsControlMode.name());
    }
}
