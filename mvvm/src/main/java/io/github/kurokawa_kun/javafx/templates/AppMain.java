package io.github.kurokawa_kun.javafx.templates;
import io.github.kurokawa_kun.javafx.templates.services.InitializationManagerImpl;
import io.github.kurokawa_kun.javafx.templates.controllers.*;
import io.github.kurokawa_kun.javafx.templates.viewmodels.*;
import javafx.application.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;
import lombok.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *   アプリのメインクラス
 */
@ConfigurationPropertiesScan("io.github.kurokawa_kun.javafx.templates")
@SpringBootApplication
@NoArgsConstructor
public class AppMain extends Application 
{
    private ConfigurableApplicationContext springContext;
    private InitializationManagerImpl initializationManager;
    private SplashController splashController;
    private SplashViewModel splashViewModel;
    private MainController mainController;
    private final BooleanProperty loadingCompleted = new SimpleBooleanProperty(this, "loadingCompleted");

    /**
     *   メインメソッド。このアプリのエントリポイント
     *   @param args コマンドライン引数
     */
    public static void main(String[] args) 
    {
        //  JavaFXを起動する
        launch(AppMain.class, args);
    }
    
    /**
     *   初期化処理
     */
    @Override
    public void init()
    {
        //  Spring Bootの起動
        springContext = SpringApplication.run(AppMain.class, getParameters().getRaw().stream().toArray(String[]::new));
        //  DIコンテナ管理下のオブジェクトを取り出す
        initializationManager = springContext.getBean(InitializationManagerImpl.class);
        splashController = springContext.getBean(SplashController.class);
        splashViewModel = springContext.getBean(SplashViewModel.class);
        mainController = springContext.getBean(MainController.class);
        loadingCompleted.set(false);
        
        //  TODO: ここに初期化処理を記述
        
        
    }

    /**
     *   アプリケーション起動時に呼び出されるメソッド
     *   @param stage 表示されるステージ
     */
    @Override
    public void start(Stage stage) throws Exception 
    {
        //  スプラッシュ画面を表示
        splashController.showWindow(stage);
        
        //  全サービスの初期化が完了したときに呼び出される処理
        this.loadingCompletedProperty().addListener((observableValue, oldValue, newValue) -> 
        {
            if (newValue) 
            {
                splashController.closeWindow();
                mainController.showWindow();
            }
        });
        
        //  TODO: ここに起動処理を記述
        
        
        
        
        //  サービスの初期化を開始する
        //  実際の初期化処理は別スレッドで動作する
        this.startLoading();
    }
    
    /**
     *   サービスの初期化を行う
     */    
    public void startLoading()
    {
        splashViewModel.startLoading();
        
        //  初期化が終わったときの処理
        initializationManager.setOnLoadingCompleted(() -> 
        {
            Platform.runLater(() -> this.loadingCompletedProperty().set(true));
        });
        
        //  初期化を開始する
        initializationManager.startLoading();
    }
    
    /**
     *   終了時に呼び出されるメソッド
     */
    public void stop()
    {
        //  TODO: ここに終了処理を記述
        
        
        springContext.close();
    }
    
    /**
     *   プロパティへのアクセサ
     */
    public boolean getLoadingCompleted()
    {
        return this.loadingCompleted.get();
    }
    public void setLoadingCompleted(boolean loadingCompleted)
    {
        this.loadingCompleted.set(loadingCompleted);
    }
    public BooleanProperty loadingCompletedProperty()
    {
        return this.loadingCompleted;
    }    
}
