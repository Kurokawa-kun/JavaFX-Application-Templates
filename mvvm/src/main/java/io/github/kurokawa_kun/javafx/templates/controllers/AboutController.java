package io.github.kurokawa_kun.javafx.templates.controllers;
import io.github.kurokawa_kun.javafx.templates.*;
import io.github.kurokawa_kun.javafx.templates.viewmodels.*;
import io.github.kurokawa_kun.javafx.templates.entities.*;
import io.github.kurokawa_kun.javafx.templates.utils.*;
import java.io.IOException;
import java.util.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.Parent;
import lombok.*;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.ObjectProvider;

/**
 *   About画面のコントローラ
 */ 
@Component
@RequiredArgsConstructor
public class AboutController
{
    private final ObjectProvider<FXMLViewLoader> loaderProvider;    
    private final ResourceLoader resourceLoader;
    private final String ASTERISM_FORMAT = "* * * * * * * * * * %s * * * * * * * * * *";
    @Getter
    private final AboutViewModel aboutViewModel;
    private final AppInfo appInfo;
    
    @FXML
    private VBox vBoxRoot;
    @FXML
    private ImageView imageViewLogo;
    @FXML
    private Label labelAppName;
    @FXML
    private Label labelAppVersion;
    @FXML
    private Label labelCompanyName;    
    @FXML
    private Text textContactInfo;    
    @FXML
    private GridPane gridPaneCmdLineArgs;
    @FXML
    private Label labelCmdLineArgs;    
    @FXML
    private GridPane gridPaneRtmInfo;
    @FXML
    private Label labelRtmInfo;    
    @FXML
    private GridPane gridPaneSysProps;
    @FXML
    private Label labelSysProps;
    @FXML
    private GridPane gridPaneEnvVars;
    @FXML
    private Label labelEnvVars;
    
    /**
     *   FXMLがロードされたときに呼び出される処理
     */
    @FXML
    public void initialize()
    {
        //  Sceneが作成されてからタイトルとアイコンを設定する
        vBoxRoot.sceneProperty().addListener((observableScene, oldScene, newScene) -> 
        {
            if (newScene != null) 
            {
                newScene.windowProperty().addListener((observableWindow, oldWindow, newWindow) -> 
                {
                    if (newWindow instanceof Stage stage) 
                    {
                        ResourceBundle resourceBundle = ResourceBundle.getBundle("io.github.kurokawa_kun.javafx.templates.i18n.messages", Locale.getDefault());
                        stage.setTitle(resourceBundle.getString("menu.help.about"));
                        stage.getIcons().addAll(appInfo.getIcons());
                    }
                });
            }        
        });
        
        //  システム情報を表示する
        //  変更されない情報のため情報取得はコントローラ側で行う
        ResourceBundle resourceBundle = ResourceBundle.getBundle("io.github.kurokawa_kun.javafx.templates.i18n.messages", Locale.getDefault());        
        labelCmdLineArgs.setText(String.format(ASTERISM_FORMAT, resourceBundle.getString("misc.commandlineargs")));
        labelRtmInfo.setText(String.format(ASTERISM_FORMAT, resourceBundle.getString("misc.runtimeinfo")));
        labelSysProps.setText(String.format(ASTERISM_FORMAT, resourceBundle.getString("misc.systemproperty")));
        labelEnvVars.setText(String.format(ASTERISM_FORMAT, resourceBundle.getString("misc.environmentvals")));        
        
        showSystemInfo(gridPaneCmdLineArgs, aboutViewModel.getDataCmdLineArgs());
        showSystemInfo(gridPaneRtmInfo, aboutViewModel.getDataRtmInfo());
        showSystemInfo(gridPaneSysProps, aboutViewModel.getDataSysProps());
        showSystemInfo(gridPaneEnvVars, aboutViewModel.getDataEnvVars());
        
        //  ビューモデルのプロパティとバインド
        imageViewLogo.imageProperty().bind(aboutViewModel.logoProperty());
        labelAppName.textProperty().bind(aboutViewModel.appNameProperty());
        labelAppVersion.textProperty().bind(aboutViewModel.appVersionProperty());
        labelCompanyName.textProperty().bind(aboutViewModel.companyNameProperty());
        textContactInfo.textProperty().bind(aboutViewModel.contactInfoProperty());        
        
        //  画面を表示する
        aboutViewModel.initialize();
    }
    
    /**  
     *   Closeボタンが押されたときの処理
     *   @param actionEvent アクションイベント
     */
    @FXML
    public void buttonCloseOnAction(ActionEvent actionEvent)
    {
        //  独自に追加したGUIオブジェクトの削除
        gridPaneRtmInfo.getChildren().clear();        
        //  ウィンドウを閉じる
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
    
    //  システム情報を画面に表示する
    private void showSystemInfo(GridPane gridPane, List<EnvironmentVariable> list)
    {
        list.forEach(l -> 
        {
            Label labelParameterName = new Label(l.name());
            labelParameterName.setWrapText(true);

            Label labelParameterValue = new Label(l.value());
            labelParameterValue.setWrapText(true);

            //  ※ TablePaneを使うと複数行表示した際に表示がずれる問題があるためGridPaneを使っている
            gridPane.addRow(gridPane.getRowCount(), labelParameterName, labelParameterValue);
            GridPane.setValignment(labelParameterName, VPos.TOP);
            GridPane.setValignment(labelParameterValue, VPos.TOP);        
        });        
    }
    
    /**  
     *   Aboutウインドウを表示する
     *   @param stage ウィンドウを表示するステージ
     */
    public void showWindow(Stage stage)
    {
        try
        {
            FXMLViewLoader loader = loaderProvider.getIfAvailable();
            ResourceBundle resourceBundle = ResourceBundle.getBundle("io.github.kurokawa_kun.javafx.templates.i18n.messages", Locale.getDefault());
            loader.setResources(resourceBundle);
            
            Parent root = loader.load(resourceLoader.getResource("classpath:io/github/kurokawa_kun/javafx/templates/fxml/aboutView.fxml").getURL().toExternalForm());            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(resourceLoader.getResource("classpath:io/github/kurokawa_kun/javafx/templates/css/style.css").getURL().toExternalForm());
            
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            dialog.setScene(scene);
            dialog.showAndWait();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
