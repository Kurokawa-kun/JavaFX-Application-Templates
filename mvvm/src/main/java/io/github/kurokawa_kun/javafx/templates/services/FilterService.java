package io.github.kurokawa_kun.javafx.templates.services;
import io.github.kurokawa_kun.javafx.templates.entities.ImageData;
import java.nio.IntBuffer;
import javafx.scene.image.*;

/**
 *   フィルタサービスのインターフェースを定義したもの
 */
public interface FilterService 
{
    /**
     *   フィルタの適用
     *   @param imageData 画像データ
     *   @return 加工後の画像
     */
    public ImageData applyEffect(ImageData imageData);
    
    /**
     *   画像をコピーする
     *   @param image コピー対象の画像
     *   @return コピーされた新しい画像
     */
    public default WritableImage copyImage(Image image)
    {
        IntBuffer intBuffer = IntBuffer.allocate((int)(image.getWidth() * image.getHeight()));
        image.getPixelReader().getPixels(0, 0, (int)image.getWidth(), (int)image.getHeight(), PixelFormat.getIntArgbInstance(), intBuffer, (int)image.getWidth());
        intBuffer.rewind();
        WritableImage newImage = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        newImage.getPixelWriter().setPixels(0, 0, (int)image.getWidth(), (int)image.getHeight(), PixelFormat.getIntArgbInstance(), intBuffer, (int)image.getWidth());
        return newImage;
    }
}
