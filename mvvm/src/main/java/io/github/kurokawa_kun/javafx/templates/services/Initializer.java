package io.github.kurokawa_kun.javafx.templates.services;

/**
 *   起動時に呼び出されるサービスのインターフェース
 */
public interface Initializer
{
    /**
     *   サービスの名前を取得する
     */
    public String getInitializationMessage();
}
