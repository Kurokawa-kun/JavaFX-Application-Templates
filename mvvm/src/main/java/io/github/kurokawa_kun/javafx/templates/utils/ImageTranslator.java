package io.github.kurokawa_kun.javafx.templates.utils;
import io.github.kurokawa_kun.javafx.templates.entities.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

/**
 *   画像の変換を行うクラス
 */
public class ImageTranslator 
{
    /**
     *   Image形式に画像を変換する
     *   @param imageData ImageData形式の画像
     *   @return 変換後の画像
     */
    public static Image getImage(ImageData imageData)
    {
        WritableImage image = new WritableImage(imageData.getWidth(), imageData.getHeight());
        PixelWriter pixelWriter = image.getPixelWriter();
        
        for (int y = 0; y < imageData.getHeight(); y++)
        {
            for (int x = 0; x < imageData.getWidth(); x++)
            {
                pixelWriter.setColor(x, y, new Color(imageData.getPixelDataAt(x, y).getRed(), imageData.getPixelDataAt(x, y).getGreen(), imageData.getPixelDataAt(x, y).getBlue(), imageData.getPixelDataAt(x, y).getTransparency()));
            }
        }
        
        return image;        
    }
    
    /**
     *   ImageData形式に画像を変換する
     *   @param image Image形式の画像
     *   @return 変換後の画像
     */
    public static ImageData getImageData(Image image)
    {
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();        
        PixelData[] pixelData = new PixelData[width * height];        
        PixelReader pixelReader = image.getPixelReader();
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                pixelData[height * y + x] = new PixelData(pixelReader.getColor(x, y).getRed(), pixelReader.getColor(x, y).getGreen(), pixelReader.getColor(x, y).getBlue(),pixelReader.getColor(x, y).getOpacity());
            }
        }
        ImageData imageData = new ImageData((int)image.getWidth(), (int)image.getHeight(), pixelData);        
        
        return imageData;
    }
}
