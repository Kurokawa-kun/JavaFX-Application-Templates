package io.github.kurokawa_kun.javafx.templates.viewmodels;
import io.github.kurokawa_kun.javafx.templates.services.InitializationManagerImpl;
import javafx.beans.property.*;
import org.springframework.stereotype.Component;

/**
 *   スプラッシュ画面のビューモデル
 */
@Component
public class SplashViewModel
{
    private final InitializationManagerImpl initializationManager;
    private final StringProperty currentMessage = new SimpleStringProperty(this, "");
    private final DoubleProperty currentProgress = new SimpleDoubleProperty(this, "");
    
    /**
     *   コンストラクタ
     *   @param initializationManager InitializationManagerのインスタンス
     */
    public SplashViewModel(InitializationManagerImpl initializationManager)
    {
        this.initializationManager = initializationManager;
    }
    
    /**
     *   サービスの初期化
     */
    public void startLoading()
    {
        this.currentMessage.bind(initializationManager.messageProperty());
        this.currentProgress.bind(initializationManager.progressProperty());
    }
    
    //  プロパティのアクセサ
    public String getCurrentMessage()
    {
        return this.currentMessage.get();
    }
    public void setCurrentMessage(String currentMessage)
    {
        this.currentMessage.setValue(currentMessage);
    }
    public StringProperty currentMessageProperty()
    {
        return this.currentMessage;
    }

    public double getCurrentProgress()
    {
        return this.currentProgress.get();
    }
    public void setCurrentProgress(double currentProgress)
    {
        this.currentProgress.set(currentProgress);
    }
    public DoubleProperty currentProgressProperty()
    {
        return this.currentProgress;
    }
}
