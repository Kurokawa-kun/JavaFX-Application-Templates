package io.github.kurokawa_kun.javafx.templates.controllers;
import io.github.kurokawa_kun.javafx.templates.entities.AppMode;
import io.github.kurokawa_kun.javafx.templates.models.MainModel;
import io.github.kurokawa_kun.javafx.templates.models.MainModelImpl;
import java.io.File;
import javafx.application.Platform;
import javafx.util.Duration;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class MainController
{
    private static final String ICON_PLAY_OFF = "/io/github/kurokawa_kun/javafx/templates/fxml/icons/play_off.png";
    private static final String ICON_PLAY_ON = "/io/github/kurokawa_kun/javafx/templates/fxml/icons/play_on.png";
    private static final String ICON_PAUSE_ON = "/io/github/kurokawa_kun/javafx/templates/fxml/icons/pause_on.png";
    private static final Image IMAGE_PLAY_OFF = new Image(MainModelImpl.class.getResource(ICON_PLAY_OFF).toExternalForm());
    private static final Image IMAGE_PLAY_ON  = new Image(MainModelImpl.class.getResource(ICON_PLAY_ON).toExternalForm());
    private static final Image IMAGE_PAUSE_ON = new Image(MainModelImpl.class.getResource(ICON_PAUSE_ON).toExternalForm());
    
    private static final Background BACKGROUND_GM = new Background(new BackgroundFill(MainModel.BACKGROUND_COLOR_GM, CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background BACKGROUND_GS = new Background(new BackgroundFill(MainModel.BACKGROUND_COLOR_GS, CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background BACKGROUND_XG = new Background(new BackgroundFill(MainModel.BACKGROUND_COLOR_XG, CornerRadii.EMPTY, Insets.EMPTY));
    
    private static final Duration ANIMATION_DURATION = Duration.seconds(2);
    
    private final MainModel mainModel;
    private AppMode appMode = AppMode.STOPPED;
    
    @FXML
    BorderPane borderPane;
    @FXML
    ToolBar toolBar;
    @FXML
    Label labelFileName;
    @FXML
    Label labelTitle;
    @FXML
    Button buttonExit;
    @FXML
    Button buttonOpenDir;
    @FXML
    Button buttonPrev;
    @FXML
    Button buttonStop;
    @FXML
    ToggleButton toggleButtonPlay;
    @FXML
    ImageView imageViewPlay;
    @FXML
    Button buttonNext;
    
    @FXML
    public void initialize()
    {
        this.borderPane.setBackground(MainController.BACKGROUND_GM);
        
        Platform.runLater(() ->
        {
            //  「閉じる」ボタンを押したときの処理を追加する
            Stage stage = (Stage)this.borderPane.getScene().getWindow();
            stage.setOnCloseRequest(event -> 
            {
                close();
            });
        });
        
        //  モードが変更されたときの処理
        this.mainModel.appModeProperty().addListener((observable, oldValue, newValue) -> 
        {
            this.appMode = newValue;
            
            Platform.runLater(() ->
            {
                this.imageViewPlay.setImage(getPlayIcon(this.appMode));
                setButtonStatus();
            });
        });
        
        //  再生中のファイル名が変更された時の処理
        this.mainModel.fileNameProperty().addListener((observable, oldValue, newValue) ->
        {
            Platform.runLater(() ->
            {
                this.labelFileName.setText(newValue);
                setButtonStatus();
            });
        });
        
        //  タイトルが変更された時の処理
        this.mainModel.titleProperty().addListener((observable, oldValue, newValue) ->
        {
            Platform.runLater(() ->
            {
                this.labelTitle.setText(newValue);
            });
        });
        
        //  背景色が変更された時の処理
        this.mainModel.backgroundColorProperty().addListener((observable, oldValue, newValue) ->
        {
            Platform.runLater(() ->
            {
                //  タイムラインを構築する
                KeyValue keyValue1 = new KeyValue(this.borderPane.backgroundProperty(), new Background(new BackgroundFill(oldValue, CornerRadii.EMPTY, Insets.EMPTY)));
                KeyFrame keyFrame1 = new KeyFrame(Duration.ZERO, keyValue1);
                KeyValue keyValue2 = new KeyValue(this.borderPane.backgroundProperty(), new Background(new BackgroundFill(newValue, CornerRadii.EMPTY, Insets.EMPTY)));
                KeyFrame keyFrame2 = new KeyFrame(ANIMATION_DURATION, keyValue2);
                Timeline timeline = new Timeline(keyFrame1, keyFrame2);
                
                //  アニメーションの実行
                timeline.play();                
            });
        });       
    }
    
    //  再生ボタンとして表示するアイコンを取得する
    private Image getPlayIcon(AppMode appMode)
    {
        return switch (appMode)
        {
            case AppMode.STOPPED -> IMAGE_PLAY_OFF;
            case AppMode.PAUSED -> IMAGE_PAUSE_ON;
            default -> IMAGE_PLAY_ON;
        };
    }
    
    /**
     *   コンストラクタ
     */
    public MainController()
    {
        mainModel = new MainModelImpl();
    }
    
    //  プログラムを終了する
    private void close()
    {
        mainModel.close();
        Stage stage = (Stage)this.borderPane.getScene().getWindow();
        
        //  すべてのウィンドウが閉じられると自動的にPlatform.exit(), Application.stop()が呼ばれる
        stage.close();
    }
    
    public void setButtonStatus()
    {
        this.buttonPrev.setDisable(this.mainModel.getPos()  <= 0);
        this.buttonNext.setDisable(this.mainModel.getPos() >= this.mainModel.getFilelist().size() - 1);
        this.buttonStop.setDisable(this.mainModel.getAppMode().isStopButtonDisable());
        this.toggleButtonPlay.setDisable(this.mainModel.getFilelist().isEmpty());
        this.toggleButtonPlay.setSelected(this.mainModel.getAppMode().isPlayButtonSelected());
    }
    
    /**  
     *   OpenDirボタンが押されたときの処理
     */
    @FXML
    public void buttonOpenDirOnAction(ActionEvent actionEvent)
    {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("MIDIファイルを開く");

        File selectedDirectory = chooser.showDialog(borderPane.getScene().getWindow());
        
        if (selectedDirectory != null)
        {
            mainModel.load(selectedDirectory.toPath());
            setButtonStatus();
        }
        else
        {
            //  キャンセルされた場合
        }
    }
    
    /**  
     *   OpenSF2ボタンが押されたときの処理
     */
    @FXML
    public void buttonOpenSF2OnAction(ActionEvent actionEvent)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("サウンドフォントを開く");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("サウンドフォント", "*.sf2");
        fileChooser.getExtensionFilters().add(extensionFilter);
        
        File selectedFile = fileChooser.showOpenDialog(borderPane.getScene().getWindow());
        if (selectedFile != null) 
        {
            //  画像データの差し替え
            mainModel.loadSoundFont(selectedFile.toPath());
        }
        else 
        {
            //  キャンセルされた場合
        }
    }
    
    /**  
     *   Exitボタンが押されたときの処理
     */
    @FXML
    public void buttonExitOnAction(ActionEvent actionEvent)
    {
        // ウィンドウの「閉じる」ボタンを押したのと同じイベント（WINDOW_CLOSE_REQUEST）を発生させる        
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));        
    }
    
    /**  
     *   Prevボタンが押されたときの処理
     */
    @FXML
    public void buttonPrevOnAction(ActionEvent actionEvent)
    {
        mainModel.prev();
        setButtonStatus();
    }
    
    /**
     *   Stopボタンが押されたときの処理
     */
    @FXML
    public void buttonStopOnAction(ActionEvent actionEvent)
    {
        mainModel.stop();
    }
    
    /**
     *   Playボタンが押されたときの処理
     */
    @FXML
    public void toggleButtonPlayOnAction(ActionEvent actionEvent)
    {
        mainModel.play();
    }
    
    /**  
     *   Nextボタンが押されたときの処理
     */
    @FXML
    public void buttonNextOnAction(ActionEvent actionEvent)
    {
        mainModel.next();
        setButtonStatus();
    }
}
