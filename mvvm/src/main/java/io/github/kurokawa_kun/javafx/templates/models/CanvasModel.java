package io.github.kurokawa_kun.javafx.templates.models;
import io.github.kurokawa_kun.javafx.templates.entities.ImageData;
import java.nio.file.Path;

/**
 *   キャンバスのモデル
 */
public interface CanvasModel 
{
    /**
     *   画像をロードする
     *   @param filePath 画像のパス
     *   @return ロードされた画像
     */
    public ImageData load(Path filePath);
    /**
     *   画像を更新する
     *   @param imageData 画像
    */
    public void updateImage(ImageData imageData);
    /**
     *   MonochromeServiceを呼び出す
     *   @return 加工された画像
     */
    public ImageData callMonochromeService();
    /**
     *   HtmlColorServiceを呼び出す
     *   @return 加工された画像
     */
    public ImageData callHtmlColorService();
}
