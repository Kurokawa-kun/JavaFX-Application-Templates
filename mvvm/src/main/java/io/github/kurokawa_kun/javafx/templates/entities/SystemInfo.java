package io.github.kurokawa_kun.javafx.templates.entities;
import java.util.List;

/**
 *   システム情報を保持するレコード
 *   @param dataCmdLineArgs コマンドライン引数
 *   @param dataRtmInfo JVMランタイム情報
 *   @param dataSysProps システムプロパティ
 *   @param dataEnvVars 環境変数
 */
public record SystemInfo(List<EnvironmentVariable> dataCmdLineArgs, List<EnvironmentVariable> dataRtmInfo, List<EnvironmentVariable> dataSysProps, List<EnvironmentVariable> dataEnvVars)
{
}
