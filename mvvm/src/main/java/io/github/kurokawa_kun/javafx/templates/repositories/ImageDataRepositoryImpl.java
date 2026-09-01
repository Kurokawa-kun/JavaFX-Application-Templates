package io.github.kurokawa_kun.javafx.templates.repositories;
import io.github.kurokawa_kun.javafx.templates.entities.ImageData;
import io.github.kurokawa_kun.javafx.templates.entities.PixelData;
import java.nio.file.Path;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import org.springframework.stereotype.Repository;

@Repository
public class ImageDataRepositoryImpl implements ImageDataRepository
{
    @Override
    public ImageData load(Path filePath)
    {
        Image image = new Image(filePath.toUri().toString());
        PixelReader pixelReader = image.getPixelReader();
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        PixelData[] pixelData = new PixelData[width * height];
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                pixelData[x + width * y] = new PixelData();
                pixelData[x + width * y].setRed(pixelReader.getColor(x, y).getRed());
                pixelData[x + width * y].setGreen(pixelReader.getColor(x, y).getGreen());
                pixelData[x + width * y].setBlue(pixelReader.getColor(x, y).getBlue());
                pixelData[x + width * y].setTransparency(pixelReader.getColor(x, y).getOpacity());
            }
        }
        
        return new ImageData(width, height, pixelData);
    }
}
