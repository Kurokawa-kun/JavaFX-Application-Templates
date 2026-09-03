import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Dimension2D;

/**
 *   起動時に表示されるウィンドウ
 *   このテンプレートは動作確認やちょっとした実験用です。
 *   モジュール形式にしていないためjlinkコマンドを使うことはできません。
 *   fxmlを用いて画面を構築する場合はコントローラクラスを別に作成してください。
 */
public class AppMain extends Application
{
    //  TODO: アプリの情報を更新すること
    public static final String APP_TITLE = "APPLICATION NAME";

    //  ウィンドウサイズ
    public final Dimension2D WINDOW_SIZE = new Dimension2D(480.0, 360.0);
    
    //  GUIコンポーネント
    private final BorderPane borderPane = new BorderPane();
    private final Label labelMessage = new Label();
    private final Button buttonUpdate = new Button();
    
    /**
     *   コンストラクタ
     */
    public AppMain()
    {
        //  TODO: ここにクラスメンバーの初期化処理を記述
        
        
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
     *   アプリ起動時に呼び出されるメソッド
     *   JavaFXのApplication終了時に呼び出されるメソッド
     *   @param 表示されるステージ
     */    
    @Override
    public void start(Stage stage)
    {
        //  ウィンドウサイズの設定
        stage.setWidth(WINDOW_SIZE.getWidth());
        stage.setHeight(WINDOW_SIZE.getHeight());
        
        //  タイトルの設定
        stage.setTitle(this.APP_TITLE);
        
        //  レイアウトの設定
        BorderPane.setAlignment(buttonUpdate, Pos.CENTER_RIGHT);
        
        //  GUIコンポーネントごとのプロパティの設定
        updateText("Click the button.");
        
        buttonUpdate.setText("Update");
        buttonUpdate.setOnAction(e -> 
        {
            updateText("Clicked!");
        });
        
        //  ウィンドウにGUIコンポーネントを追加
        borderPane.setCenter(labelMessage);
        borderPane.setBottom(buttonUpdate);

        //  TODO: ここにアプリの起動処理を記述
        
        
        
        //  画面表示
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     *   アプリ終了時に呼び出されるメソッド
     */
    public void stop()
    {
        //  TODO: ここにアプリの終了処理を記述
        
        
    }
    
    //  ラベルのテキストを更新する
    private void updateText(String name)
    {
        labelMessage.setText(name);
    }
    
    /**
     *   メインメソッド  
     */
    public static void main(String[] args) 
    {
        //  JavaFXアプリの起動
        launch();
    }
}
