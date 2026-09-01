package io.github.kurokawa_kun.javafx.templates.entities;

/**
 *   環境変数を格納するレコード
 */
public record EnvironmentVariable(String name, String value)
{
    public static EnvironmentVariable NONE = new EnvironmentVariable("", "(NONE)");
}
