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
     *   表示倍率を取得する
     *   @return 倍率
     */
    public double getZoomRatio();
    /**
     *   表示倍率を設定する。実際の倍率は 2 ^ (ratio - 4) 倍になる
     *   @param ratio 倍率
     */
    public void setZoomRatio(double ratio);        
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
