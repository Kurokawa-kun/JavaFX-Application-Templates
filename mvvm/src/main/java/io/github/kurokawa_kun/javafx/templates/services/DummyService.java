package io.github.kurokawa_kun.javafx.templates.services;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import org.springframework.stereotype.Service;

/**
 *   ダミーのサービス
 */
@Service
public class DummyService extends Task<Object> implements Initializer
{
    //  TODO: ここに起動中のメッセージを記入
    private final String SERVICE_NAME = "Dummy Service";
    
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
        Object o = initSomething();
        return null;
    }
    
    //  実際の初期化処理
    private Object initSomething()
    {
        Object[] obj = new Object[10];
        for (int c = 0; c < obj.length; c++)
        {
            try
            {
                Thread.sleep(300);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            updateMessage(getInitializationMessage());
            updateProgress((double)c, (double)obj.length);
        }        
        return null;
    }
}
