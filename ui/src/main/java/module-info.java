module driverstation.main {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;

    requires gson;
    requires castle;
    requires org.controlsfx.controls;
    requires slf4j.api;
    requires argparse4j;
    requires flashlib;
    requires java.logging;
    requires java.sql;
    requires jsdl2;
    requires jsdl2.jni;
    requires notifier;

    requires driverstation.libcomp;
    requires driverstation.api;

    exports com.flash3388.frc.ds;
    exports com.flash3388.frc.ds.ui;
}