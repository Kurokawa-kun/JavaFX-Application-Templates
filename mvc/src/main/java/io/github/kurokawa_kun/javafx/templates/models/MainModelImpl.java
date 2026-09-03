package io.github.kurokawa_kun.javafx.templates.models;
import io.github.kurokawa_kun.javafx.templates.controllers.*;
import io.github.kurokawa_kun.javafx.templates.services.*;
import io.github.kurokawa_kun.javafx.templates.repositories.*;
import java.util.*;
import java.nio.file.Path;
import javax.sound.midi.*;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.*;

public class MainModelImpl implements MainModel
{
    private static final String ICON_PLAY_OFF = "/io/github/kurokawa_kun/javafx/templates/fxml/icons/play_off.png";
    private static final String ICON_PLAY_ON = "/io/github/kurokawa_kun/javafx/templates/fxml/icons/play_on.png";
    private static final String ICON_PAUSE_ON = "/io/github/kurokawa_kun/javafx/templates/fxml/icons/pause_on.png";
    private static final Image IMAGE_PLAY_OFF = new Image(MainModelImpl.class.getResource(ICON_PLAY_OFF).toExternalForm());
    private static final Image IMAGE_PLAY_ON  = new Image(MainModelImpl.class.getResource(ICON_PLAY_ON).toExternalForm());
    private static final Image IMAGE_PAUSE_ON = new Image(MainModelImpl.class.getResource(ICON_PAUSE_ON).toExternalForm());
    
    private static final byte[] GM_RESET = {(byte)0xF0, (byte)0x7E, (byte)0x7F, (byte)0x09, (byte)0x01, (byte)0xF7};
    private static final byte[] GM2_RESET = {(byte)0xF0, (byte)0x7E, (byte)0x7F, (byte)0x09, (byte)0x03, (byte)0xF7};
    private static final byte[] GS_RESET = {(byte)0xF0, (byte)0x41, (byte)0x10, (byte)0x42, (byte)0x12, (byte)0x40, (byte)0x00, (byte)0x7F, (byte)0x00, (byte)0x41, (byte)0xF7};
    private static final byte[] GS_SYSTEM_MODE1_SET = {(byte)0xF0, (byte)0x41, (byte)0x10, (byte)0x42, (byte)0x12, (byte)0x00, (byte)0x00, (byte)0x7F, (byte)0x00, (byte)0x01, (byte)0xF7};
    private static final byte[] GS_SYSTEM_MODE2_SET = {(byte)0xF0, (byte)0x41, (byte)0x10, (byte)0x42, (byte)0x12, (byte)0x00, (byte)0x00, (byte)0x7F, (byte)0x01, (byte)0x00, (byte)0xF7};
    private static final byte[] XG_SYSTEM_ON = {(byte)0xF0, (byte)0x43, (byte)0x10, (byte)0x4C, (byte)0x00, (byte)0x00, (byte)0x7E, (byte)0x00, (byte)0xF7};
    
    private static final Color BACKGROUND_COLOR_GM = Color.ALICEBLUE;
    private static final Color BACKGROUND_COLOR_GS = Color.DARKORANGE;
    private static final Color BACKGROUND_COLOR_XG = Color.LAWNGREEN;
    
    private final IntervalTimer intervalTimer = new IntervalTimer();
    private final ColorChanger colorChanger;
    
    @Getter
    private List<Path> filelist = new ArrayList<>(3000);
    @Getter @Setter
    private int pos;
    @Getter @Setter
    private AppMode appMode = AppMode.STOPPED;
    private MidiPlayerImpl midiPlayer;
    private final MidiFileRepositoryImpl midiFilesRepository;
    private final SoundFontRepositoryImpl soundFontRepository;
    private final ObjectProperty<Background> backGroundProperty = new SimpleObjectProperty<>(new Background(new BackgroundFill(BACKGROUND_COLOR_GM, CornerRadii.EMPTY, Insets.EMPTY)));
    private final ObjectProperty<Color> colorProperty = new SimpleObjectProperty<>();
    private final BooleanProperty toggleButtonSelectedProperty = new SimpleBooleanProperty(false);
    private final StringProperty fileNameProperty = new SimpleStringProperty();
    private final StringProperty titleProperty = new SimpleStringProperty();
    private final BooleanProperty prevButtonDisableProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty stopButtonDisableProperty = new SimpleBooleanProperty(true);
    private final BooleanProperty playButtonDisableProperty = new SimpleBooleanProperty(false);
    private final ObjectProperty<Image> playButtonImageProperty = new SimpleObjectProperty<>(IMAGE_PLAY_OFF);
    private final BooleanProperty nextButtonDisableProperty = new SimpleBooleanProperty(false);
    
    //  再生ボタンとして表示するアイコンを取得する
    private Image getPlayIcon(AppMode appMode)
    {
        return switch (appMode)
        {
            case AppMode.STOPPED -> IMAGE_PLAY_OFF;
            case AppMode.PAUSED -> IMAGE_PAUSE_ON;
            default -> IMAGE_PLAY_ON;
        };
    }
    
    //  シーケンサーの初期化
    private void initSequencer(Soundbank soundbank)
    {
        if (soundbank != null)
        {
            midiPlayer.setSoundbank(soundbank);
        }
        
        //  メタメッセージをリアルタイムで調べる
        midiPlayer.getSequencer().addMetaEventListener(metaMessage ->         
        {
            Platform.runLater(() ->
            {
                int type = metaMessage.getType();
                byte[] data = metaMessage.getData();

                switch (type) 
                {
                    case 0x03 -> 
                    {
                        String trackName = new String(data);
                        this.titleProperty.set(trackName);
                    }
                    case 0x2F -> 
                    {
                        interval();
                    }
                }
            });
        });
        
        try
        {
            midiPlayer.getSequencer().getTransmitter().setReceiver(new Receiver()
            {
                @Override
                public void send(MidiMessage message, long timeStamp)
                {
                    if (message instanceof SysexMessage) 
                    {
                        byte[] data = message.getMessage();
                        
                        if (Arrays.equals(data, GM_RESET) || Arrays.equals(data, GM2_RESET)) 
                        {
                            Platform.runLater(() -> 
                            {
                                colorChanger.setCurrentColor(getColor());
                                colorChanger.setTargetColor(BACKGROUND_COLOR_GM);
                                colorChanger.restart();
                            });
                        }
                        else if (Arrays.equals(data, GS_RESET) || Arrays.equals(data, GS_SYSTEM_MODE1_SET) || Arrays.equals(data, GS_SYSTEM_MODE2_SET)) 
                        {
                            Platform.runLater(() -> 
                            {
                                colorChanger.setCurrentColor(getColor());
                                colorChanger.setTargetColor(BACKGROUND_COLOR_GS);
                                colorChanger.restart();
                            });
                        }
                        else if (Arrays.equals(data, XG_SYSTEM_ON)) 
                        {
                            Platform.runLater(() -> 
                            {
                                colorChanger.setCurrentColor(getColor());
                                colorChanger.setTargetColor(BACKGROUND_COLOR_XG);
                                colorChanger.restart();
                            });
                        }
                    }
                }
                @Override
                public void close() 
                {
                }
            });
        }
        catch (MidiUnavailableException e)
        {
            e.printStackTrace();
        }
        
    }
    
    /**
     *   コンストラクタ
     *   @param mainController コントローラ
     */
    public MainModelImpl(MainController mainController)
    {
        soundFontRepository = new SoundFontRepositoryImpl();
        midiFilesRepository = new MidiFileRepositoryImpl();
        midiPlayer = new MidiPlayerImpl();        
        initSequencer(null);
        
        //  インターバルタイマーが終了したときの処理を登録する
        intervalTimer.setOnSucceeded(event -> 
        {
            if (getPos() == getFilelist().size() - 1)
            {
                stop();
            }
            else
            {
                next();
            }
        });
        
        this.colorProperty.set((Color)this.backGroundProperty.get().getFills().get(0).getFill());        
        
        colorChanger = new ColorChanger(this.colorProperty);
        this.colorProperty().addListener((observable, oldValue, newValue) -> 
        {
            setBackground(new Background(new BackgroundFill(newValue, CornerRadii.EMPTY, Insets.EMPTY)));
        });
    }
    
    @Override
    public void setButtonDisability()
    {
        setPrevButtonDisable(pos <= 0);
        setNextButtonDisable(pos >= this.filelist.size() - 1);
        setPlayButtonDisable(this.filelist.size() == 0);        
    }
    
    @Override
    public void close()
    {
        midiPlayer.closeMidiPlayer();
    }
    
    //  指定したモードに設定する
    private void applyAppMode(AppMode appMode, Path file)
    {
        setAppMode(appMode);
        setFileName(file.getFileName().toString());
        setStopButtonDisable(getAppMode().disableStopButton());
        setPlayButtonImage(getPlayIcon(getAppMode()));
        switch (getAppMode())
        {
            case STOPPED ->
            {
                setToggleButtonSelected(false);
                this.midiPlayer.stop();
            }
            case PLAYING ->
            {
                setToggleButtonSelected(true);
                this.midiPlayer.stop();
                this.midiPlayer.play(file);
            }
            case RESUMED ->
            {
                setToggleButtonSelected(true);
                this.midiPlayer.play(file);
            }
            case PAUSED ->
            {
                setToggleButtonSelected(true);
                this.midiPlayer.pause();
            }
            case INTERVAL ->
            {
                //  PLAYING状態から遷移するパターンしかないため、画面表示に変更はない
                this.midiPlayer.stop();
            }
        }
    }    
    
    @Override
    public void load(Path path)
    {
        pos = 0;
        
        //  ファイル名の一覧を取得
        this.filelist = midiFilesRepository.loadFiles(path);
        
        if (!getFilelist().isEmpty())
        {
            //  .midファイルが存在する
            applyAppMode(AppMode.PLAYING, getFilelist().get(getPos()));
        }
        else
        {
            //  指定したディレクトリ配下に.midファイルが1つも存在しなかった場合
            //  何もしない
            applyAppMode(AppMode.STOPPED, null);
        }

        //  「戻る」「進む」ボタンの非表示を設定する
        setButtonDisability();
    }
    
    @Override
    public void prev()
    {
        pos--;
        setTitle("");
        this.intervalTimer.cancel();
        setButtonDisability();
        
        switch (getAppMode())
        {
            case AppMode.RESUMED -> 
            {
                setAppMode(AppMode.PLAYING);
            }
            case AppMode.PAUSED -> 
            {
                setAppMode(AppMode.PLAYING);
            }
            case AppMode.INTERVAL -> 
            {
                setAppMode(AppMode.PLAYING);
            }
        }        
        applyAppMode(getAppMode(), getFilelist().get(getPos()));
    }
    
    @Override
    public void stop()
    {
        this.intervalTimer.cancel();
        setAppMode(AppMode.STOPPED);
        applyAppMode(getAppMode(), getFilelist().get(getPos()));        
    }
    
    @Override
    public void next()
    {
        pos++;        
        setTitle("");
        this.intervalTimer.cancel();
        setButtonDisability();        
        switch (getAppMode())
        {
            case AppMode.RESUMED -> 
            {
                setAppMode(AppMode.PLAYING);
            }
            case AppMode.PAUSED -> 
            {
                setAppMode(AppMode.PLAYING);
            }
            case AppMode.INTERVAL -> 
            {
                setAppMode(AppMode.PLAYING);
            }
        }        
        applyAppMode(getAppMode(), getFilelist().get(getPos()));
    }
    
    @Override
    public void play()
    {
        //  モードの変更
        switch (getAppMode())  //  現在のモード
        {
            case AppMode.STOPPED -> 
            {
                setAppMode(AppMode.PLAYING);
                applyAppMode(getAppMode(), filelist.get(pos));
            }
            case AppMode.PLAYING -> 
            {
                setAppMode(AppMode.PAUSED);
                applyAppMode(getAppMode(), filelist.get(pos));
            }
            case AppMode.RESUMED -> 
            {
                setAppMode(AppMode.PAUSED);
                applyAppMode(getAppMode(), filelist.get(pos));
            }
            case AppMode.PAUSED -> 
            {
                setAppMode(AppMode.RESUMED);
                applyAppMode(getAppMode(), filelist.get(pos));
            }
            case AppMode.INTERVAL -> 
            {
                this.intervalTimer.cancel();
                setAppMode(AppMode.STOPPED);
                applyAppMode(getAppMode(), filelist.get(pos));
            }
        }
    }
    
    //  インターバルモード（曲の最後から次の曲までのスリープ時間）に切り替える
    private void interval()
    {
        this.intervalTimer.restart();
        setAppMode(AppMode.INTERVAL);
        applyAppMode(getAppMode(), filelist.get(pos));
    }
    
    @Override
    public void loadSoundFont(Path path)
    {
        initSequencer(soundFontRepository.getSoundFont(path));
    }
    
    public Background getBackground()
    {
        return this.backGroundProperty.get();
    }
    public void setBackground(Background background)
    {
        this.backGroundProperty.set(background);
    }
    public ObjectProperty<Background> backGroundProperty()
    {
        return this.backGroundProperty;
    }
    
    public Color getColor()
    {
        return this.colorProperty.get();
    }
    public void setColor(Color color)
    {
        this.colorProperty.set(color);
    }
    public ObjectProperty<Color> colorProperty()
    {
        return this.colorProperty;
    }
    
    public boolean getToggleButtonSelected()
    {
        return this.toggleButtonSelectedProperty.get();
    }    
    public void setToggleButtonSelected(boolean selected)
    {
        this.toggleButtonSelectedProperty.set(selected);
    }    
    public BooleanProperty toggleButtonSelectedProperty()            
    {
        return this.toggleButtonSelectedProperty;
    }
    
    public String getFileName()
    {
        return this.fileNameProperty.get();
    }
    public void setFileName(String fileName)
    {
        this.fileNameProperty.set(fileName);
    }
    public StringProperty fileNameProperty()            
    {
        return this.fileNameProperty;
    }
    
    public String getTitle()
    {
        return this.titleProperty.get();
    }
    public void setTitle(String title)
    {
        this.titleProperty.set(title);
    }
    public StringProperty titleProperty()
    {
        return this.titleProperty;
    }
    
    public boolean getPrevButtonDisable()
    {
        return this.prevButtonDisableProperty.get();
    }
    public void setPrevButtonDisable(boolean disable)
    {
        this.prevButtonDisableProperty.set(disable);
    }
    public BooleanProperty prevButtonDisableProperty()
    {
        return this.prevButtonDisableProperty;
    }
    
    public boolean getStopButtonDisable()
    {
        return this.stopButtonDisableProperty.get();
    }
    public void setStopButtonDisable(boolean disable)
    {
        this.stopButtonDisableProperty.set(disable);
    }
    public BooleanProperty stopButtonDisableProperty()
    {
        return this.stopButtonDisableProperty;
    }
    
    public boolean getPlayButtonDisable()
    {
        return this.playButtonDisableProperty.get();
    }
    public void setPlayButtonDisable(boolean disable)
    {
        this.playButtonDisableProperty.set(disable);
    }
    public BooleanProperty playButtonDisableProperty()
    {
        return this.playButtonDisableProperty;
    }    
    
    public Image getPlayButtonImage()
    {
        return this.playButtonImageProperty.get();
    }
    public void setPlayButtonImage(Image image)
    {
        this.playButtonImageProperty.set(image);
    }
    public ObjectProperty<Image> playButtonImageProperty()
    {
        return this.playButtonImageProperty;
    }
    
    public boolean getNextButtonDisable()
    {
        return this.nextButtonDisableProperty.get();
    }
    public void setNextButtonDisable(boolean disable)
    {
        this.nextButtonDisableProperty.set(disable);
    }
    public BooleanProperty nextButtonDisableProperty()
    {
        return this.nextButtonDisableProperty;
    }
}
