package io.github.kurokawa_kun.javafx.templates;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Dimension2D;

/**
 *   起動時に表示されるウィンドウ
 *   fxmlを用いて画面を構築する場合はコントローラクラスを別に作成してください。
 */
public class AppMain extends Application
{
    //  ウィンドウサイズ
    public final Dimension2D WINDOW_SIZE = new Dimension2D(480.0, 360.0);
    
    //  GUIコンポーネント
    private final AnchorPane anchorPane = new AnchorPane();
    private final VBox vBoxMain = new VBox();
    private final VBox vBoxSub = new VBox();
    private final Label labelHelloWorld = new Label();
    private final TextField textFieldName = new TextField();
    private final Button buttonUpdate = new Button();
    private final Button buttonClear = new Button();
    
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
        //  TODO: ここにアプリの初期化処理を記述。このメソッドはstartメソッドより先に呼び出されJavaFXのスレッドとは別で動作する
        
        
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
        
        //  タイトルとアイコンの設定
        stage.setTitle(AppInfo.APP_TITLE);
        stage.getIcons().addAll(AppInfo.ICONS);
        
        //  レイアウトの設定
        AnchorPane.setTopAnchor(vBoxMain, 0.0d);
        AnchorPane.setLeftAnchor(vBoxMain, 0.0d);        
        AnchorPane.setTopAnchor(vBoxSub, 0.0d);
        AnchorPane.setRightAnchor(vBoxSub, 0.0d);
        HBox.setHgrow(labelHelloWorld, Priority.ALWAYS);
        labelHelloWorld.setMaxWidth(Double.MAX_VALUE);
        
        //  GUIコンポーネントごとのプロパティの設定
        updateText("");
        
        buttonUpdate.setMaxWidth(Double.MAX_VALUE);
        buttonUpdate.setText("Update");
        buttonUpdate.setOnAction(e -> 
        {
            updateText(textFieldName.getText());
        });
        
        buttonClear.setMaxWidth(Double.MAX_VALUE);
        buttonClear.setText("Clear");
        buttonClear.setOnAction(e -> 
        {
            textFieldName.setText("");
            updateText(textFieldName.getText());
        });
        
        //  ウィンドウにGUIコンポーネントを追加
        vBoxMain.getChildren().add(labelHelloWorld);        
        vBoxMain.getChildren().add(textFieldName);        
        anchorPane.getChildren().add(vBoxMain);
        vBoxSub.getChildren().add(buttonUpdate);
        vBoxSub.getChildren().add(buttonClear);
        anchorPane.getChildren().add(vBoxSub);
        
        //  TODO: ここにアプリの起動処理を追加
        
        
        
        //  画面表示
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     *   アプリ終了時に呼び出されるメソッド
     */
    public void stop()
    {
        //  TODO: ここにアプリの終了処理を追加
        
        
    }
    
    //  ラベルのテキストを更新する
    private void updateText(String name)
    {
        String newName = name.isEmpty() ? "World" : textFieldName.getText();
        labelHelloWorld.setText(String.format("Hello, %s.", newName));
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
