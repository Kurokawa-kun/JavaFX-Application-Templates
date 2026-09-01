package io.github.kurokawa_kun.javafx.templates.controllers;
import io.github.kurokawa_kun.javafx.templates.*;
import io.github.kurokawa_kun.javafx.templates.viewmodels.*;
import io.github.kurokawa_kun.javafx.templates.utils.*;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.fxml.*;
import lombok.*;
import org.springframework.core.io.ResourceLoader;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SplashController
{
    private final AppInfo appInfo;
    private final ObjectProvider<FXMLViewLoader> loaderProvider;    
    private final SplashViewModel splashViewModel;
    private final ResourceLoader resourceLoader;
    
    @FXML
    private VBox vBoxRoot;
    @FXML
    private ImageView imageViewLogo;
    @FXML
    private Label labelAppName;
    @FXML
    private Label labelAppVersion;
    @FXML
    private ProgressBar progressBarCurrentProgress;
    @FXML
    private Label labelCurrentMessage;
    
    /**
     *   FXMLがロードされたときに呼び出される処理
     */
    @FXML
    public void initialize() 
    {
        //  GUIコンポーネントの各種設定
        labelAppName.setText(appInfo.getName());
        labelAppVersion.setText(appInfo.getVersion());
        imageViewLogo.setImage(appInfo.getLogo());
        progressBarCurrentProgress.progressProperty().bind(splashViewModel.currentProgressProperty());
        labelCurrentMessage.textProperty().bind(splashViewModel.currentMessageProperty());        
        
        //  Sceneが作成されてからタイトルとアイコンを設定する
        vBoxRoot.sceneProperty().addListener((observableScene, oldScene, newScene) -> 
        {
            if (newScene != null) 
            {
                newScene.windowProperty().addListener((observableWindow, oldWindow, newWindow) -> 
                {
                    if (newWindow instanceof Stage stage) 
                    {
                        stage.setTitle(appInfo.getName());
                        stage.getIcons().addAll(appInfo.getIcons());
                    }
                });
            }        
        });        
    }
    
    /**
     *   スプラッシュ画面の表示
     *   @param stage ウィンドウを表示するステージ
     */
    public void showWindow(Stage stage)
    {
        try
        {
            FXMLViewLoader loader = loaderProvider.getIfAvailable();
            ResourceBundle resourceBundle = ResourceBundle.getBundle("io.github.kurokawa_kun.javafx.templates.i18n.messages", Locale.getDefault());
            loader.setResources(resourceBundle);
            
            Parent root = loader.load(resourceLoader.getResource("classpath:io/github/kurokawa_kun/javafx/templates/fxml/splashView.fxml").getURL().toExternalForm());            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(resourceLoader.getResource("classpath:io/github/kurokawa_kun/javafx/templates/css/style.css").getURL().toExternalForm());
            
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED); // 枠なし
            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     *   ウィンドウを閉じる
     */
    public void closeWindow()
    {
        // Stageを取得して閉じる
        Stage stage = (Stage)vBoxRoot.getScene().getWindow();
        stage.close();
    }
}
