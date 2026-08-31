package io.github.kurokawa_kun.javafx.templates.controllers;
import io.github.kurokawa_kun.javafx.templates.models.MainModel;
import java.io.File;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.fxml.*;
import javafx.stage.FileChooser;

public class MainController
{
    private final MainModel mainModel;
    
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
        //  プロパティのバインド
        borderPane.backgroundProperty().bind(this.mainModel.backGroundProperty());
        toggleButtonPlay.selectedProperty().bindBidirectional(this.mainModel.toggleButtonSelectedProperty());
        labelFileName.textProperty().bind(this.mainModel.fileNameProperty());
        labelTitle.textProperty().bind(this.mainModel.titleProperty());
        buttonPrev.disableProperty().bind(this.mainModel.prevButtonDisableProperty());
        buttonStop.disableProperty().bind(this.mainModel.stopButtonDisableProperty());
        toggleButtonPlay.disableProperty().bind(this.mainModel.playButtonDisableProperty());
        imageViewPlay.imageProperty().bind(this.mainModel.playButtonImageProperty());
        buttonNext.disableProperty().bind(this.mainModel.nextButtonDisableProperty());
        
        Platform.runLater(() ->
        {
            //  「前へ」「次へ」のボタンを選択不可にする
            mainModel.setButtonDisability();
            
            //  「閉じる」ボタンを押したときの処理を追加する
            Stage stage = (Stage)this.borderPane.getScene().getWindow();
            stage.setOnCloseRequest(event -> 
            {
                mainModel.close();
            });
        });        
    }
    
    /**
     *   コンストラクタ
     */
    public MainController()
    {
        mainModel = new MainModel(this);
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
        mainModel.close();        
        Stage stage = (Stage)this.borderPane.getScene().getWindow();
        stage.close();
    }
    
    /**  
     *   Prevボタンが押されたときの処理
     */
    @FXML
    public void buttonPrevOnAction(ActionEvent actionEvent)
    {
        mainModel.prev();
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
    }
}
