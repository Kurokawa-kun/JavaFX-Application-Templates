module FXMLJavaFXApplicationApp
{
    requires java.desktop;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires lombok;
    opens io.github.kurokawa_kun.javafx.templates to javafx.fxml;
}
