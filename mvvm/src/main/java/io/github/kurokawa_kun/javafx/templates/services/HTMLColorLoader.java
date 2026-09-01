package io.github.kurokawa_kun.javafx.templates.services;
import java.util.*;
import java.io.*;
import java.text.MessageFormat;
import javafx.scene.paint.Color;
import javafx.concurrent.Task;
import lombok.*;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HTMLColorLoader extends Task<Object> implements Initializer
{     
    private final String SERVICE_NAME = "HTML Color Service";
    private final int NumberOfColor =147;
    private Map<String, Color> mapHTMLColorNames;
    private final String CSV_FILE_NAME = "io/github/kurokawa_kun/javafx/templates/csvfiles/HTMLColorNames.csv";
    private final ResourceLoader resourceLoader;
    
    @Override
    public String getInitializationMessage()
    {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("io.github.kurokawa_kun.javafx.templates.i18n.messages", Locale.getDefault());
        String message = MessageFormat.format(resourceBundle.getString("msg.service.start"), SERVICE_NAME);
        return message;
    }   
    
    /**
     *   サービスの初期化処理
     *   @return 呼び出し側に返却するオブジェクト
     */
    @Override
    protected Object call()
    {
        updateProgress(0.0d, 1.0d);
        updateMessage(getInitializationMessage());
        
        //  HTMLカラーパレットが記載されたcsvファイルを取得する
        mapHTMLColorNames = readCSV(CSV_FILE_NAME);
        
        return mapHTMLColorNames;
    }
    
    //  指定されたcsvファイルを読み込んでマップ形式にする
    private Map<String, Color> readCSV(String filename)
    {
        Map<String, Color> data = new HashMap(NumberOfColor);
        
        ResourceBundle resourceBundle = ResourceBundle.getBundle("io.github.kurokawa_kun.javafx.templates.i18n.messages", Locale.getDefault());
        try (InputStream inputStream = resourceLoader.getResource("classpath:" + filename).getInputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)))
        {
            if (inputStream == null)
            {
                String message = MessageFormat.format(resourceBundle.getString("msg.file.error.filenotfound"), CSV_FILE_NAME);
                System.err.println(message);
                return null;
            }
            reader.lines().forEach(c -> data.put(c.split(",")[0], Color.web(c.split(",")[0])));
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        return data;
    }    
}
