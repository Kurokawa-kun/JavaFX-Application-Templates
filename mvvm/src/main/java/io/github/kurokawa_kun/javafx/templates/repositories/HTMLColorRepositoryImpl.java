package io.github.kurokawa_kun.javafx.templates.repositories;
import java.util.Map;
import java.util.HashMap;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Repository;

@Repository
public class HTMLColorRepositoryImpl implements HTMLColorRepository
{
    private Map<String, Color> htmlColor = new HashMap<>();
    
    @Override
    public void setHTMLColor(Map<String, Color> htmlColor)
    {
        this.htmlColor = htmlColor;
    }
    @Override
    public Map<String, Color> getHTMLColor()
    {
        return this.htmlColor;
    }
}
