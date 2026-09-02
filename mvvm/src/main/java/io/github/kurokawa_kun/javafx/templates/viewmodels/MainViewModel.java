package io.github.kurokawa_kun.javafx.templates.viewmodels;
import io.github.kurokawa_kun.javafx.templates.entities.ImageData;
import io.github.kurokawa_kun.javafx.templates.models.CanvasModelImpl;
import io.github.kurokawa_kun.javafx.templates.services.*;
import io.github.kurokawa_kun.javafx.templates.utils.ImageTranslator;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.*;
import javafx.beans.property.*;
import javafx.scene.image.*;
import org.springframework.stereotype.Component;

/**
 *   Mainウィンドウのビューモデル
 */
@Component
public class MainViewModel 
{
    private final DoubleProperty sliderValueProperty = new SimpleDoubleProperty();
    private final DoubleProperty imageScaleProperty = new SimpleDoubleProperty();
    private final ObjectProperty<String> comboBoxValueProperty = new SimpleObjectProperty<>();
    private final StringProperty labelZoomRatioProperty = new SimpleStringProperty();
    private final BooleanProperty sliderDisableProperty = new SimpleBooleanProperty(true);
    private final ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
    private final CanvasModelImpl canvasModel;
    
    /**
     *   コンストラクタ
     *   @param canvasModel キャンバスモデル
     *   @param monochromeService MonochromeServiceのインスタンス
     *   @param htmlColorService HtmlColorServiceのインスタンス
     */
    public MainViewModel(CanvasModelImpl canvasModel, MonochromeService monochromeService, HtmlColorLoader htmlColorService)
    {
        this.canvasModel = canvasModel;
        this.sliderValueProperty.addListener((observable, oldValue, newValue) -> 
        {
            this.changeZoomRatio(newValue.doubleValue());
        });
        this.imageProperty.set(new WritableImage(1, 1));        
    }
    
    /**
     *   画像をコピーする
     *   @param image コピー対象の画像
     *   @return コピーされた新しい画像
     */
    public WritableImage copyImage(Image image)
    {
        IntBuffer intBuffer = IntBuffer.allocate((int)(image.getWidth() * image.getHeight()));
        image.getPixelReader().getPixels(0, 0, (int)image.getWidth(), (int)image.getHeight(), PixelFormat.getIntArgbInstance(), intBuffer, (int)image.getWidth());
        intBuffer.rewind();
        WritableImage newImage = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        newImage.getPixelWriter().setPixels(0, 0, (int)image.getWidth(), (int)image.getHeight(), PixelFormat.getIntArgbInstance(), intBuffer, (int)image.getWidth());
        return newImage;
    }    
    
    /**
     *   ImageViewの画像を更新する
     *   @param image 新しい画像
     */
    public void updateImage(Image image)
    {
        //  表示している画像データの差し替え
        this.setCurrentImage(image);
    }
    
    /**
     *   ImageViewの画像を更新する
     *   @param image 新しい画像
     */
    public void loadImage(Path path)
    {
        //  表示している画像データの差し替え
        ImageData imageData = this.canvasModel.load(path);
        Image image = ImageTranslator.getImage(imageData);
        this.updateImage(image);
        changeZoomRatio(4.0);
        this.sliderDisableProperty.set(false);
    }
    
    /**
     *   MonochromeServiceを呼び出す
     */
    public void callMonochromeService()
    {
        ImageData imageData = this.canvasModel.callMonochromeService();
        Image image = ImageTranslator.getImage(imageData);
        this.updateImage(image);
    }
    
    /**
     *   HtmlColorServiceを呼び出す
     */
    public void callHtmlColorService()
    {
        ImageData imageData = this.canvasModel.callHtmlColorService();
        Image image = ImageTranslator.getImage(imageData);
        this.updateImage(image);
    }
    
    /**
     *   表示倍率を変更する
     *   @param sliderValue 表示倍率を指定する。実際の倍率は 2 ^ (sliderValue - 4) 倍になる。
     */
    public void changeZoomRatio(double sliderValue)
    {
        //  画像表示の倍率を変更する
        double actualRatio = Math.pow(2, sliderValue - 4);
        this.canvasModel.setZoomRatio(actualRatio);
        this.setImageScaleProperty(actualRatio);
        this.setSliderValueProperty(sliderValue);
        
        //  テキストを変更する
        ResourceBundle resourceBundle = ResourceBundle.getBundle("io.github.kurokawa_kun.javafx.templates.i18n.messages", Locale.getDefault());
        String text = MessageFormat.format(resourceBundle.getString("misc.unit.percent"), String.format("%2.0f ", (100.0 * actualRatio)));
        this.labelZoomRatioProperty.set(text);
    }
    
    public ObjectProperty<Image> imageProperty()
    {
        return this.imageProperty;
    }
    public Image getCurrentImage()
    {
        return this.imageProperty.get();
    }
    public void setCurrentImage(Image image)
    {
        this.imageProperty.set(image);
    }
    
    public double getSliderValueProperty()
    {
        return this.sliderValueProperty.get();
    }
    public void setSliderValueProperty(double value)
    {
        this.sliderValueProperty.set(value);
    }
    public DoubleProperty sliderValueProperty()
    {
        return this.sliderValueProperty;
    }
    
    public double getImageScaleProperty()
    {
        return this.imageScaleProperty.get();
    }
    public void setImageScaleProperty(double actualZoomRatio)
    {
        this.imageScaleProperty.set(actualZoomRatio);
    }
    public DoubleProperty imageScaleProperty()
    {
        return this.imageScaleProperty;
    }
    
    public String getComboBoxValue()
    {
        return this.comboBoxValueProperty.get();
    }
    public void setComboBoxValue(String comboBoxValue)
    {
        this.comboBoxValueProperty.set(comboBoxValue);
    }
    public ObjectProperty comboBoxValueProperty()
    {
        return this.comboBoxValueProperty;
    }
    
    public String getLabelZoomRatio()
    {
        return this.labelZoomRatioProperty.get();
    }
    public void setLabelZoomRatio(String text)
    {
        this.labelZoomRatioProperty.set(text);
    }
    public StringProperty labelZoomRatioProperty()
    {
        return this.labelZoomRatioProperty;
    }
    
    public boolean getSliderDisable()
    {
        return this.sliderDisableProperty.get();
    }
    public void setSliderDisable(boolean disable)
    {
        this.sliderDisableProperty.set(disable);
    }
    public BooleanProperty sliderDisableProperty()
    {
        return this.sliderDisableProperty;
    }
}
