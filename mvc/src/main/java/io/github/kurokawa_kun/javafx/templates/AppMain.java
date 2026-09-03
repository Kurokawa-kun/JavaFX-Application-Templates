package io.github.kurokawa_kun.javafx.templates;
import java.io.IOException;
import java.net.URL;
import javafx.stage.*;
import javafx.scene.*;
import javafx.application.*;
import javafx.fxml.*;
import static javafx.application.Application.launch;

public class AppMain extends Application
{
    /**
     *   コンストラクタ
     */
    public AppMain()
    {
    }
    
    /**
     *   初期化処理
     */
    @Override
    public void init()
    {
        //  TODO: ここに初期化処理を記述
        
        
    }
    
    /**
     *   アプリケーション起動時に呼び出されるメソッド
     *   @param stage 表示されるステージ
     */
    public void start(Stage stage)
    {
        //  画面表示
        FXMLLoader loader = new FXMLLoader();
        try
        {
            loader.setLocation(getClass().getResource("/io/github/kurokawa_kun/javafx/templates/fxml/mainView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.getScene().getStylesheets().add(getClass().getResource("/io/github/kurokawa_kun/javafx/templates/css/style.css").toExternalForm());
            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        //  タイトルとアイコンの設定
        stage.setTitle(AppInfo.APP_TITLE);
        stage.getIcons().addAll(AppInfo.ICONS);
        
        //  TODO: ここに起動処理を記述
        
        
    }
    
    /**
     *   終了時に呼び出されるメソッド
     */
    public void stop()
    {
        //  TODO: ここに終了処理を記述
        
        
    }
    
    /**
     *   メインメソッド  
     */
    public static void main(String[] args) 
    {
        launch();   //  JavaFXアプリケーションの起動
    }    
}
