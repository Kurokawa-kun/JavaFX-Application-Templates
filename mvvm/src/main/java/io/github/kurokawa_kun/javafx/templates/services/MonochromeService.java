package io.github.kurokawa_kun.javafx.templates.services;
import io.github.kurokawa_kun.javafx.templates.entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MonochromeService implements FilterService
{
    @Override
    public ImageData applyEffect(ImageData imageData)
    {
        int imageWidth = imageData.getWidth();
        int imageHeight = imageData.getHeight();
        ImageData newImage = new ImageData(imageWidth, imageHeight);
        
        for (int y = 0; y < newImage.getHeight(); y++)
        {
            for (int x = 0; x < newImage.getWidth(); x++)
            {
                double m = 0.2126 * imageData.getPixelDataAt(x, y).getRed() + 0.7152 * imageData.getPixelDataAt(x, y).getGreen() + 0.0722 * imageData.getPixelDataAt(x, y).getBlue();
                newImage.setPixelDataAt(x, y, new PixelData(m, m, m, 1.0));
            }
        }
        
        return newImage;
    }
}
