package com.flash3388.frc.ds.robot;

public enum RobotControlMode {
    TELEOPERATED {
        @Override
        public String displayName() {
            return "OperatorControl";
        }
    },
    AUTONOMOUS {
        @Override
        public String displayName() {
            return "Autonomous";
        }
    },
    PRACTICE {
        @Override
        public String displayName() {
            return "Practice";
        }
    },
    TEST {
        @Override
        public String displayName() {
            return "Test";
        }
    }
    ;

    public abstract String displayName();
}
