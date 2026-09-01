package io.github.kurokawa_kun.javafx.templates.models;
import io.github.kurokawa_kun.javafx.templates.entities.*;
import java.lang.management.*;
import java.util.*;
import java.util.stream.Collectors;
import javafx.collections.*;
import lombok.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
/**
 *   システム情報のモデル
 */    
public class SystemInfoModel implements CommandLineRunner
{
    @Getter
    private final SystemInfo systemInfo;    
    private final List<EnvironmentVariable> dataCmdLineArgs = new ArrayList<>();
    private final List<EnvironmentVariable> dataRtmInfo = new ArrayList<>();
    private final List<EnvironmentVariable> dataSysProps = new ArrayList<>();
    private final List<EnvironmentVariable> dataEnvVars = new ArrayList<>();
    
    /**
     *   コンストラクタ
     */    
    public SystemInfoModel()
    {
        //  SystemInfoの構築
        this.systemInfo = new SystemInfo(dataCmdLineArgs, dataRtmInfo, dataSysProps, dataEnvVars);
        
        //  コマンドライン引数の取得はここでは行わない（行えない）
        getRuntimeInformation().forEach(dataRtmInfo::add);
        getSystemProperties().forEach(dataSysProps::add);
        getEnvironmentVariables().forEach(dataEnvVars::add);
    }
    
    /**
     *   CommandLineRunnerのバッチ処理（コマンドライン引数を取得するために使用）
     *   @param args コマンドライン引数
     */
    @Override
    public void run(String... args) throws Exception
    {
        //  コンストラクタ呼び出しの時点ではCommandLineRunnerが呼び出されていないため、ここで呼び出す
        getCommandLineArguments(args).forEach(dataCmdLineArgs::add);
    }
    
    //  このアプリのコマンドライン引数をリストにして返す
    private List<EnvironmentVariable>getCommandLineArguments(String[] args)
    {
        return Arrays.stream(args).map(s -> 
        {
            int index = s.indexOf("=");
            String name = (index != -1) ? s.substring(0, index) : s;
            String value = (index != -1) ? s.substring(index + 1) : "";
            return new EnvironmentVariable(name, value);
        }).collect(Collectors.toList());
    }
    
    //  JVMのランタイム情報をリストにして返す
    private ObservableList<EnvironmentVariable> getRuntimeInformation() 
    {
        return ManagementFactory.getRuntimeMXBean().getInputArguments().stream()
            .map(arg -> arg.split("=", 2))
            .map(parts -> new EnvironmentVariable(
                parts[0], 
                parts.length > 1 ? parts[1] : ""
            ))
            .sorted(Comparator.comparing(EnvironmentVariable::name))
            .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    
    //  動作環境の情報を一覧にして返す
    private ObservableList<EnvironmentVariable> getSystemProperties() 
    {
        return System.getProperties().entrySet().stream()
            .map(e -> (Map.Entry<String, String>) (Map.Entry<?, ?>) e)
            .sorted(Map.Entry.comparingByKey())
            .map(e -> new EnvironmentVariable(e.getKey(), e.getValue()))
            .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    
    //  OSの環境変数を一覧にして返す
    private ObservableList<EnvironmentVariable> getEnvironmentVariables()
    {
        return Arrays.stream(new String[]
        {
            "PATH", "CLASSPATH", "JAVA_HOME", "JAVA_EXEC", "JAVAFX_HOME", "JAVA_TOOL_OPTIONS", "_JAVA_OPTIONS", "JDK_HOME", "JDK_JAVA_OPTIONS", 
            "ANT_HOME", "ANT_OPTS", "ANT_ARGS", "MAVEN_HOME", "M2_HOME", "MAVEN_OPTS", "MAVEN_CONFIG", "MAVEN_ARGS", "MAVEN_DEBUG_OPTS", 
            "GRADLE_HOME", "GRADLE_USER_HOME", "GRADLE_OPTS", "SPRING_PROFILES_ACTIVE", "SPRING_CONFIG_LOCATION", "SPRING_CONFIG_IMPORT", "SPRING_APPLICATION_JSON"            
        })
        .map(key -> new AbstractMap.SimpleEntry<>(key, System.getenv(key)))
        .filter(entry -> entry.getValue() != null)
        .sorted(Map.Entry.comparingByKey())
        .map(entry -> new EnvironmentVariable(entry.getKey(), entry.getValue()))
        .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
}
