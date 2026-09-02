package io.github.kurokawa_kun.javafx.templates.models;
import io.github.kurokawa_kun.javafx.templates.entities.*;
import io.github.kurokawa_kun.javafx.templates.services.*;
import io.github.kurokawa_kun.javafx.templates.repositories.*;
import java.nio.file.Path;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
public class CanvasModelImpl
{
    @Getter @Setter
    private ImageData imageData;
    @Getter @Setter
    private double zoomRatio;
    private final ImageDataRepository imageDataRepository;
    private final MonochromeService monochromeService;
    private final HtmlColorService htmlColorService;
    
    public CanvasModelImpl(ImageDataRepository imageDataRepository, MonochromeService monochromeService, HtmlColorService htmlColorService)
    {
        this.imageData = new ImageData(1, 1);
        this.imageDataRepository = imageDataRepository;
        this.monochromeService = monochromeService;
        this.htmlColorService = htmlColorService;
    }
    
    public ImageData load(Path filePath)
    {
        ImageData newImageData = this.imageDataRepository.load(filePath);
        updateImage(newImageData);
        return newImageData;
    }
        
    public void updateImage(ImageData imageData)
    {
        this.setImageData(imageData);
    }
    
    public ImageData callMonochromeService()
    {
        ImageData newImageData = monochromeService.applyEffect(this.imageData);
        updateImage(newImageData);
        return newImageData;
    }
    
    public ImageData callHtmlColorService()
    {
        ImageData newImageData = htmlColorService.applyEffect(this.imageData);
        updateImage(newImageData);
        return newImageData;
    }
}
