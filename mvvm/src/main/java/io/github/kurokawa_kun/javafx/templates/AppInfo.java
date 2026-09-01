package io.github.kurokawa_kun.javafx.templates;
import java.util.List;
import javafx.scene.image.Image;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

/**
 *   アプリの共通情報を保持するクラス
 */
@ConfigurationProperties(prefix = "app.info")
@Getter
public class AppInfo
{
    //  TODO: リソースフォルダにある以下のファイルを修正すること
    //  - application.yml
    //  - imagesディレクトリ配下
    private final String name;
    private final String version;
    private final String authorName;
    private final List<String> contactInfo;
    
    // YAMLから読み込むパス情報
    private final List<String> iconPaths;    
    private final String logoPath;
    private final String splashPath;
    
    // Imageのリスト（コンストラクタで作成する）
    private final List<Image> icons;
    private final Image logo;
    private final Image splash;
    
    /**
     *   コンストラクタ
     *   @param name アプリ名
     *   @param version バージョン
     *   @param authorName 作者名
     *   @param contactInfo 連絡先
     *   @param iconPaths アイコンファイルのパス
     *   @param logoPath ロゴ画像へのパス
     *   @param splashPath スプラッシュ画像へのパス
     */
    @ConstructorBinding
    public AppInfo(String name, String version, String authorName, List<String> contactInfo, List<String> iconPaths, String logoPath, String splashPath)
    {
        this.name = name;
        this.version = version;
        this.authorName = authorName;
        this.contactInfo = contactInfo;
        this.iconPaths = iconPaths;
        this.logoPath = logoPath;
        this.splashPath = splashPath;
        
        this.icons = this.iconPaths.stream().map(path -> new Image(getClass().getResourceAsStream(path))).toList();
        this.logo = new Image(getClass().getResourceAsStream(logoPath));
        this.splash = new Image(getClass().getResourceAsStream(splashPath));
    }    
}
