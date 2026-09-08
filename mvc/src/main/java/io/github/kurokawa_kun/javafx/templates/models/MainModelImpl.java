package io.github.kurokawa_kun.javafx.templates.models;
import io.github.kurokawa_kun.javafx.templates.entities.*;
import io.github.kurokawa_kun.javafx.templates.services.*;
import io.github.kurokawa_kun.javafx.templates.repositories.*;
import java.util.*;
import java.nio.file.Path;
import javax.sound.midi.*;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import lombok.*;

public class MainModelImpl implements MainModel
{
    private final IntervalTimer intervalTimer = new IntervalTimer();
    private final ObjectProperty<Color> backgroundColor = new SimpleObjectProperty<>(MainModel.BACKGROUND_COLOR_GM);
    
    @Getter
    private List<Path> filelist = new ArrayList<>(3000);
    @Getter
    private int pos;
    private final StringProperty fileNameProperty = new SimpleStringProperty("");
    private final StringProperty titleProperty = new SimpleStringProperty("");
    private final ObjectProperty<AppMode> appModePropety = new SimpleObjectProperty<>(AppMode.STOPPED);
    private final ObjectProperty<Color> colorProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Background> backGroundProperty = new SimpleObjectProperty<>(new Background(new BackgroundFill(BACKGROUND_COLOR_GM, CornerRadii.EMPTY, Insets.EMPTY)));
    
    private final MidiPlayerImpl midiPlayer;
    private final MidiFileRepository midiFilesRepository;
    private final SoundFontRepository soundFontRepository;
    
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
                        setTitle(trackName);
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
                            setBackgroundColor(MainModel.BACKGROUND_COLOR_GM);
                        }
                        else if (Arrays.equals(data, GS_RESET) || Arrays.equals(data, GS_SYSTEM_MODE1_SET) || Arrays.equals(data, GS_SYSTEM_MODE2_SET)) 
                        {
                            setBackgroundColor(MainModel.BACKGROUND_COLOR_GS);
                        }
                        else if (Arrays.equals(data, XG_SYSTEM_ON)) 
                        {
                            setBackgroundColor(MainModel.BACKGROUND_COLOR_XG);
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
     */
    public MainModelImpl()
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
        switch (getAppMode())
        {
            case STOPPED ->
            {
                this.midiPlayer.stop();
            }
            case PLAYING ->
            {
                this.midiPlayer.stop();
                this.midiPlayer.play(file);
            }
            case RESUMED ->
            {
                this.midiPlayer.play(file);
            }
            case PAUSED ->
            {
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
            Path newFile = getFilelist().get(getPos());
            applyAppMode(AppMode.PLAYING, newFile);
            setFileName(newFile.getFileName().toString());
        }
        else
        {
            //  指定したディレクトリ配下に.midファイルが1つも存在しなかった場合
            //  何もしない
            applyAppMode(AppMode.STOPPED, null);
        }
    }
    
    @Override
    public void prev()
    {
        pos--;
        setTitle("");
        this.intervalTimer.cancel();
        
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
        setPos(pos + 1);
        setTitle("");
        this.intervalTimer.cancel();
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
    
    private void setPos(int pos)
    {
        this.pos = pos;
    }
    
    public String getFileName()
    {
        return this.fileNameProperty.get();
    }
    private void setFileName(String fileName)
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
    private void setTitle(String title)
    {
        this.titleProperty.set(title);
    }
    public StringProperty titleProperty()
    {
        return this.titleProperty;
    }    
    
    public AppMode getAppMode()
    {
        return this.appModePropety.get();
    }
    private void setAppMode(AppMode appMode)
    {
        this.appModePropety.set(appMode);
    }
    public ObjectProperty<AppMode> appModeProperty()
    {
        return this.appModePropety;
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
    
    @Override
    public Color getBackgroundColor()
    {
        return this.backgroundColor.get();
    }
    private void setBackgroundColor(Color backgroundColor)
    {
        this.backgroundColor.set(backgroundColor);
    }
    @Override
    public ObjectProperty<Color> backgroundColorProperty()
    {
        return this.backgroundColor;
    }
}
