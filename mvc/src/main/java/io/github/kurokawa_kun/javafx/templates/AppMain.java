package io.github.kurokawa_kun.javafx.templates;
import java.io.IOException;
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
        //  TODO: ここに初期化処理を記述。このメソッドはJavaFXとは別スレッドで動作するため重い処理はここに書く
    }
    
    /**
     *   アプリ起動時に呼び出されるメソッド。このメソッドはJavaFXのスレッドで動作する
     *   JavaFXのApplication終了時に呼び出されるメソッド
     *   @param stage 表示されるステージ
     */    
    @Override
    public void start(Stage stage)
    {
        //  画面表示
        FXMLLoader loader = new FXMLLoader();
        try
        {
            loader.setLocation(getClass().getClassLoader().getResource("io/github/kurokawa_kun/javafx/templates/fxml/MainView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.getScene().getStylesheets().add(ClassLoader.getSystemResource("io/github/kurokawa_kun/javafx/templates/css/style.css").toExternalForm());
            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        //  タイトルとアイコンの設定
        stage.setTitle(AppInfo.APP_TITLE);
        stage.getIcons().addAll(AppInfo.ICONS);
        
        //  TODO: ここに実行したい処理を追加
        
        
    }
    
    /**
     *   終了時に呼び出されるメソッド
     */
    public void stop()
    {
    }
    
    /**
     *   メインメソッド  
     */
    public static void main(String[] args) 
    {
        launch();   //  JavaFXアプリケーションの起動
    }    
}
