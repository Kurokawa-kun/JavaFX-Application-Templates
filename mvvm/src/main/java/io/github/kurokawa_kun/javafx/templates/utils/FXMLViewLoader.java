package io.github.kurokawa_kun.javafx.templates.utils;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.fxml.*;
import javafx.scene.Parent;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

/**
 *   FXMLLoaderを継承したクラス
 *   コントローラファクトリを差し替えてコントローラをSpring管理下に置けるようにする
 */
@Component
@Scope("prototype")
public class FXMLViewLoader extends FXMLLoader
{
    private final ApplicationContext springContext;
    
    /**
     *   コンストラクタ
     *   @param springContext Springコンテキスト
     */
    public FXMLViewLoader(ApplicationContext springContext) 
    {
        super();
        this.springContext = springContext;
    }
    
    /**
     *   FXMLファイルをロードする
     *   @param fxmlPath FXMLファイルへのパス
     *   @return ルートのノード
     */
    public Parent load(String fxmlPath)
    {
        try
        {
            this.setLocation(new URI(fxmlPath).toURL());
            //  ControllerをSpringの管理下から取得するように設定する
            this.setControllerFactory(springContext::getBean);
            return this.load();
        }
        catch (MalformedURLException | URISyntaxException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
