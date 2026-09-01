module mvvm
{
    requires spring.boot.starter;
    requires spring.boot.starter.logging;
    requires spring.boot.autoconfigure;
    requires jakarta.annotation;
    requires org.yaml.snakeyaml;
    requires ch.qos.logback.classic;
    requires org.apache.logging.log4j.to.slf4j;
    requires jul.to.slf4j;
    requires spring.boot;
    requires ch.qos.logback.core;
    requires org.slf4j;
    requires org.apache.logging.log4j;
    requires spring.context;
    requires spring.aop;
    requires spring.beans;
    requires spring.expression;
    requires spring.core;
    requires micrometer.observation;
    requires micrometer.commons;
    requires org.jspecify;
    requires biz.aQute.bnd.annotation;
    requires com.google.errorprone.annotations;
    requires org.osgi.annotation.bundle;
    requires org.osgi.annotation.versioning;
    requires org.osgi.resource;
    requires org.apache.commons.logging;    
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.management;
    requires static lombok;

    opens io.github.kurokawa_kun.javafx.templates to spring.beans, spring.context, javafx.fxml;
    opens io.github.kurokawa_kun.javafx.templates.controllers to spring.beans, spring.context, javafx.fxml;    
}
