package io.github.kurokawa_kun.javafx.templates.controllers;
import io.github.kurokawa_kun.javafx.templates.*;
import io.github.kurokawa_kun.javafx.templates.utils.*;
import io.github.kurokawa_kun.javafx.templates.viewmodels.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import lombok.*;
import org.springframework.core.io.ResourceLoader;
import org.springframework.beans.factory.*;
import org.springframework.stereotype.Component;

/**
 *   Mainウィンドウのコントローラ
 */
@Component
@RequiredArgsConstructor
public class MainController
{
    private final ObjectProvider<FXMLViewLoader> loaderProvider;
    private final AboutController aboutController;
    private final AppInfo appInfo;
    private final MainViewModel mainViewModel;
    private final ResourceLoader resourceLoader;
    
    @FXML
    private BorderPane borderPane;
    @FXML
    private MenuItem menuItemExit;
    @FXML
    private MenuItem menuItemAbout;
    @FXML
    private MenuItem menuItemMonochrome;
    @FXML
    private MenuItem menuItemHtmlColor;
    @FXML
    private ScrollPane scrollPaneCurrentImage;
    @FXML
    private ImageView imageViewCurrentImage;
    @FXML
    private Slider sliderZoomRatio;
    @FXML
    private Label labelZoomRatio;
    
    /**
     *   FXMLがロードされたときに呼び出される処理
     */
    @FXML
    public void initialize()
    {
        //  Sceneが作成されてからタイトルとアイコンを設定する
        borderPane.sceneProperty().addListener((observableScene, oldScene, newScene) -> 
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
        
        Platform.runLater(() ->
        {
            //  「閉じる」ボタンを押したときの処理を追加する
            Stage stage = (Stage)this.borderPane.getScene().getWindow();
            stage.setOnCloseRequest(event -> 
            {
                close();
            });
        });        
        
        //  TODO: fxmlのロードが完了した時点で実行したい処理をここに記述
        setupDragAndDrop();
        
        //  ビューモデルとのバインド
        imageViewCurrentImage.imageProperty().bind(mainViewModel.imageProperty());
        imageViewCurrentImage.scaleXProperty().bindBidirectional(mainViewModel.imageScaleProperty());
        imageViewCurrentImage.scaleYProperty().bindBidirectional(mainViewModel.imageScaleProperty());        
        sliderZoomRatio.disableProperty().bind(mainViewModel.sliderDisableProperty());
        labelZoomRatio.textProperty().bind(mainViewModel.labelZoomRatioProperty());        
        sliderZoomRatio.valueProperty().bindBidirectional(mainViewModel.sliderValueProperty());
    }
    
    @FXML
    //  Openメニューが押されたときの処理
    private void menuItemOpenOnAction()
    {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("io.github.kurokawa_kun.javafx.templates.i18n.messages", Locale.getDefault());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resourceBundle.getString("menu.file.open"));
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(resourceBundle.getString("misc.file.filetype.image"), "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extensionFilter);
        
        File selectedFile = fileChooser.showOpenDialog(borderPane.getScene().getWindow());
        if (selectedFile != null) 
        {
            //  画像データの差し替え
            mainViewModel.loadImage(Path.of(selectedFile.toURI()));
        }
        else 
        {
            //  キャンセルされた場合
        }
    }
    
    //  プログラムを終了する
    private void close()
    {
        Stage stage = (Stage)this.borderPane.getScene().getWindow();
        //  すべてのウィンドウが閉じられると自動的にPlatform.exit(), Application.stop()が呼ばれる
        stage.close();
    }
    
    @FXML
    //  Closeメニューが押されたときの処理
    private void menuItemExitOnAction()
    {
        // ウィンドウの「閉じる」ボタンを押したのと同じイベント（WINDOW_CLOSE_REQUEST）を発生させる        
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));        
    }
    
    @FXML
    //  Aboutメニューが押されたときの処理
    private void menuItemAboutOnAction() throws IOException
    {
        Stage stage = (Stage)borderPane.getScene().getWindow();
        aboutController.showWindow(stage);
    }
    
    @FXML    
    //  Monochromeメニューが押されたときの処理
    private void menuItemMonochromeOnAction()
    {
        mainViewModel.callMonochromeService();
    }
    
    @FXML    
    //  HtmlColorメニューが押されたときの処理
    private void menuItemHtmlColorOnAction()
    {
        mainViewModel.callHtmlColorService();
    }
    
    //  ドラッグ＆ドロップの処理を設定する
    private void setupDragAndDrop() 
    {
        //  ドラッグされたときの処理
        scrollPaneCurrentImage.setOnDragOver(event -> 
        {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles() && isImageFile(dragboard.getFiles().get(0)))
            {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        //  ドロップされたときの処理
        scrollPaneCurrentImage.setOnDragDropped(event -> 
        {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;

            if (dragboard.hasFiles()) 
            {
                Path path = Path.of(dragboard.getFiles().get(0).toURI());
                mainViewModel.loadImage(path);
                success = true;
            }
            // ドロップ処理が成功したかどうかをシステムに通知
            event.setDropCompleted(success);
            event.consume();
        });
    }

    //  拡張子が画像かチェックする
    private boolean isImageFile(File file) 
    {
        String name = file.getName().toLowerCase();
        return (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"));
    }
    
    /**  
     *   Mainウインドウを表示する
     */
    public void showWindow()
    {
        try
        {
            FXMLViewLoader loader = loaderProvider.getIfAvailable();
            ResourceBundle resourceBundle = ResourceBundle.getBundle("io.github.kurokawa_kun.javafx.templates.i18n.messages", Locale.getDefault());
            loader.setResources(resourceBundle);
            
            Parent root = loader.load(resourceLoader.getResource("classpath:io/github/kurokawa_kun/javafx/templates/fxml/mainView.fxml").getURL().toExternalForm());            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(resourceLoader.getResource("classpath:io/github/kurokawa_kun/javafx/templates/css/style.css").getURL().toExternalForm());
            
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
