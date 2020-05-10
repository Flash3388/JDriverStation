module driverstation.main {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;

    requires castle;
    requires org.controlsfx.controls;

    requires driverstation.libcomp;
    requires driverstation.api;

    exports com.flash3388.frc.ds;
    exports com.flash3388.frc.ds.ui;
}