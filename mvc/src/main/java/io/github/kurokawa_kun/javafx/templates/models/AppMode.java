package io.github.kurokawa_kun.javafx.templates.models;

//  アプリの動作モード
public enum AppMode
{
    STOPPED(false, true, true),
    PLAYING(true, false, false),
    RESUMED(true, false, false),
    PAUSED(false, false, false),
    INTERVAL(false, false, false);
    
    private final boolean playable;
    private final boolean disableStopButton;
    private final boolean clearInfo;
    
    AppMode(boolean playable, boolean disableStopButton, boolean clearInfo) 
    {
        this.playable = playable;
        this.disableStopButton = disableStopButton;
        this.clearInfo = clearInfo;
    }
    
    public boolean isPlayable()
    {
        return this.playable;
    }
    
    public boolean disableStopButton()
    {
        return this.disableStopButton;
    }
    
    public boolean isClearInfo()
    {
        return this.isClearInfo();
    }
}
