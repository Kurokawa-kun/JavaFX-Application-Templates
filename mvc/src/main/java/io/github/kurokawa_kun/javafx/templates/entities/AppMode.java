package io.github.kurokawa_kun.javafx.templates.entities;
import java.util.List;
import java.util.ArrayList;

//  アプリの動作モード
public enum AppMode
{
    STOPPED(false, false, true),
    PLAYING(true, true, false),
    RESUMED(true, true, false),
    PAUSED(false, true, false),
    INTERVAL(false, true, false);
    
    private final boolean playable;
    private final boolean playButtonSelected;
    private final boolean stopButtonDisabled;
    
    AppMode(boolean playable, boolean playButtonSelected, boolean stopButtonDisabled) 
    {
        this.playable = playable;
        this.playButtonSelected = playButtonSelected;
        this.stopButtonDisabled = stopButtonDisabled;
    }
    
    public boolean isPlayable()
    {
        return this.playable;
    }
    
    public boolean isPlayButtonSelected()
    {
        return this.playButtonSelected;
    }
    
    public boolean isStopButtonDisable()
    {
        return this.stopButtonDisabled;
    }    
}
