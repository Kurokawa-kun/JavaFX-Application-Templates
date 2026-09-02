package io.github.kurokawa_kun.javafx.templates.services;
import io.github.kurokawa_kun.javafx.templates.entities.*;
import io.github.kurokawa_kun.javafx.templates.repositories.*;
import java.util.*;
import javafx.scene.paint.Color;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HtmlColorService implements FilterService
{
    private final HtmlColorRepository htmlColorRepository;
    private Map<String, Color> mapHtmlColorNames;
    
    @Override
    public ImageData applyEffect(ImageData imageData)
    {
        mapHtmlColorNames = htmlColorRepository.getHtmlColor();
        int imageWidth = imageData.getWidth();
        int imageHeight = imageData.getHeight();
        PixelData[] pixelData = imageData.getPixelData();
        
        ImageData newImage = new ImageData(imageWidth, imageHeight, new PixelData[imageWidth * imageHeight]);
        
        for (int y = 0; y < newImage.getHeight(); y++)
        {
            for (int x = 0; x < newImage.getWidth(); x++)
            {
                Color oldColor = imageData.getPixelDataAt(x, y).getColor();
                Color newColor = getNearlestColor(oldColor, mapHtmlColorNames);
                newImage.setPixelDataAt(x, y, new PixelData(newColor));
            }
        }
        
        return newImage;
    }
    
    //  指定された色に最も近いHTMLカラーを返却する
    //  （処理速度を優先してアルゴリズムを手抜きしているので最も近くない場合もあります）
    private Color getNearlestColor(Color color, Map<String, Color> colorNames)
    {
        double[] distances = new double[colorNames.size()];
        int c = 0;
        for (String colorSet : colorNames.keySet())
        {
            distances[c] = getDistance(color, colorNames.get(colorSet));
            if (distances[c] <= 0.15d) return colorNames.get(colorSet);
            c++;
        }
        
        int minIndex = 0;
        for (int i = 1; i < distances.length; i++) 
        {
            if (distances[i] < distances[minIndex]) 
            {
                minIndex = i;
            }
        }
        
        return colorNames.values().stream().skip(minIndex).findFirst().orElse(null);
    }
    
    //  指定された色同士の距離を求める
    private double getDistance(Color c1, Color c2)
    {
        return Math.sqrt(Math.pow(c2.getRed() - c1.getRed(), 2) + Math.pow(c2.getGreen() - c1.getGreen(), 2) + Math.pow(c2.getBlue() - c1.getBlue(), 2));
    }
}
