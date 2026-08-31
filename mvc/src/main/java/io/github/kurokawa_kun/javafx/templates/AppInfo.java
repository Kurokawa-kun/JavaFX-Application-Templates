package io.github.kurokawa_kun.javafx.templates;
import javafx.scene.image.Image;

public class AppInfo
{
    //  TODO: アプリの情報を更新すること
    public static final String APP_TITLE = "APPLICATION NAME";
    public static final Image[] ICONS = 
    {
        new Image(AppInfo.class.getResourceAsStream("images/icon16.png")),
        new Image(AppInfo.class.getResourceAsStream("images/icon32.png")),
        new Image(AppInfo.class.getResourceAsStream("images/icon48.png")),
        new Image(AppInfo.class.getResourceAsStream("images/icon64.png")),
        new Image(AppInfo.class.getResourceAsStream("images/icon128.png"))
    };
}
