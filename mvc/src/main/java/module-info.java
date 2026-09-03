module mvc
{
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    opens io.github.kurokawa_kun.javafx.templates.controllers to javafx.fxml;
    exports io.github.kurokawa_kun.javafx.templates;
    exports io.github.kurokawa_kun.javafx.templates.controllers;
}
