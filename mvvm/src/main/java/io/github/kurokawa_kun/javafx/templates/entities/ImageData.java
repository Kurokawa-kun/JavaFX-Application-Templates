package io.github.kurokawa_kun.javafx.templates.entities;
import javafx.scene.paint.Color;
import lombok.*;

/**
 *   画像データ
 */
public class ImageData
{
    @Getter
    private final int width;
    @Getter
    private final int height;
    @Getter
    PixelData[] pixelData;
    
    /**
     *   コンストラクタ
     *   @param width 画像の幅
     *   @param height 画像の高さ
     *   @param pixelData ピクセルデータ
     */
    public ImageData(int width, int height, PixelData[] pixelData)
    {
        this.width = width;
        this.height = height;
        this.pixelData = pixelData;
    }
    
    /**
     *   コンストラクタ
     *   @param width 画像の幅
     *   @param height 画像の高さ
     */
    public ImageData(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.pixelData = new PixelData[width * height];
        
        for (int y = 0; y < this.height; y++)
        {
            for (int x = 0; x < this.width; x++)
            {
                this.pixelData[this.width * y + x] = new PixelData(Color.BLACK);
            }
        }
    }
    
    /**
     *   指定した座標のピクセルデータを取得する
     *   @x X座標
     *   @y Y座標
     */
    public PixelData getPixelDataAt(int x, int y)
    {
        return pixelData[this.width * y + x];
    }
    
    /**
     *   指定した座標のピクセルデータを設定する
     *   @x X座標
     *   @y Y座標
     *   @pixelData ピクセルデータ
     */
    public void setPixelDataAt(int x, int y, PixelData pixelData)
    {
        this.pixelData[this.width * y + x] = pixelData;
    }
}
