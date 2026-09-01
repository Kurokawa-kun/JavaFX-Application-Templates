package io.github.kurokawa_kun.javafx.templates.entities;
import javafx.scene.paint.Color;

/**
 *   ピクセルデータ
 */
public class PixelData
{
    private byte red;
    private byte green;
    private byte blue;
    private byte transparency;
    
    /**
     *   コンストラクタ
     */
    public PixelData()
    {
        this.red = -128;
        this.green = -128;
        this.blue = -128;
        this.transparency = -128;
    }    
    /**
     *   コンストラクタ
     *   @param red 赤
     *   @param green 緑
     *   @param blue 青
     *   @param transparency 透明度
     */
    public PixelData(int red, int green, int blue, int transparency)
    {
        this.red = (byte) (red - 128);
        this.green = (byte) (green - 128);
        this.blue = (byte) (blue - 128);
        this.transparency = (byte) (transparency - 128);
    }    
    /**
     *   コンストラクタ
     *   @param red 赤
     *   @param green 緑
     *   @param blue 青
     *   @param transparency 透明度
     */
    public PixelData(double red, double green, double blue, double transparency)
    {
        this.red = (byte) Math.round(red * 255.0 - 128);
        this.green = (byte) Math.round(green * 255.0 - 128);
        this.blue = (byte) Math.round(blue * 255.0 - 128);
        this.transparency = (byte) Math.round(transparency * 255.0 - 128);
    }
    /**
     *   コンストラクタ
     *   @param color 色
     */
    public PixelData(Color color)
    {
        this(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
    }
    /**
     *   コンストラクタ
     */
    public Color getColor()
    {
        return new Color(this.getRed(), this.getGreen(), this.getBlue(), this.getTransparency());
    }
    /**
     *   赤成分の取得
     */
    public double getRed()
    {
        return (this.red + 128) / 255.0;
    }
    /**
     *   緑成分の取得
     */
    public double getGreen()
    {
        return (this.green + 128) / 255.0;
    }
    /**
     *   青成分の取得
     */
    public double getBlue()
    {
        return (this.blue + 128) / 255.0;
    }
    /**
     *   透明成分の取得
     */
    public double getTransparency()
    {
        return (this.transparency + 128) / 255.0;
    }
    /**
     *   色の設定
     */
    public void setColor(Color color)
    {
        this.red = (byte) Math.round(color.getRed() * 255.0 - 128);
        this.green = (byte) Math.round(color.getGreen() * 255.0 - 128);
        this.blue = (byte) Math.round(color.getBlue() * 255.0 - 128);
        this.transparency = (byte) Math.round(color.getOpacity() * 255.0 - 128);
    }
    /**
     *   赤成分の設定
     */
    public void setRed(int red)
    {
        this.red = (byte) (red - 128);
    }
    /**
     *   緑成分の設定
     */
    public void setGreen(int green)
    {
        this.green = (byte) (green - 128);
    }
    /**
     *   青成分の設定
     */
    public void setBlue(int blue)
    {
        this.blue = (byte) (blue - 128);
    }
    /**
     *   透明成分の設定
     */
    public void setTransparency(int transparency)
    {
        this.transparency = (byte) (transparency - 128);
    }
    /**
     *   赤成分の設定
     */
    public void setRed(double red)
    {
        this.red = (byte) Math.round(red * 255.0 - 128);
    }
    /**
     *   緑成分の設定
     */
    public void setGreen(double green)
    {
        this.green = (byte) Math.round(green * 255.0 - 128);
    }
    /**
     *   青成分の設定
     */
    public void setBlue(double blue)
    {
        this.blue = (byte) Math.round(blue * 255.0 - 128);
    }
    /**
     *   透明成分の設定
     */
    public void setTransparency(double transparency)
    {
        this.transparency = (byte) Math.round(transparency * 255.0 - 128);
    }    
}
