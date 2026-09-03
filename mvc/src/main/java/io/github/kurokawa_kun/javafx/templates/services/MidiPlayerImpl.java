package io.github.kurokawa_kun.javafx.templates.services;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import javax.sound.midi.*;
import lombok.*;

//  MIDIプレイヤー
public class MidiPlayerImpl implements MidiPlayer
{    
    @Getter
    private Sequencer sequencer = null;    
    private long tickPosition = 0;
    @Getter
    private Soundbank soundbank = null;
    private Synthesizer synthesizer = null;
    private Path currentMidiFile;
    
    /**
     *   コンストラクタ
     */
    public MidiPlayerImpl()
    {
        try
        {
            this.sequencer = MidiSystem.getSequencer(false);
            this.sequencer.open();
            this.synthesizer = MidiSystem.getSynthesizer();
            this.synthesizer.open();
            this.soundbank = this.synthesizer.getDefaultSoundbank();            
            //  シンセサイザーとシーケンサーを接続
            this.getSequencer().getTransmitter().setReceiver(this.synthesizer.getReceiver());
        }
        catch (MidiUnavailableException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void setSoundbank(Soundbank soundbank)
    {
        boolean running = this.sequencer.isRunning();
        if (running)
        {
            stop();
        }
        
        if (!synthesizer.isSoundbankSupported(soundbank)) 
        {
            System.err.println("このサウンドフォントはサポートされていません。");
            return;
        }
        
        this.synthesizer.unloadAllInstruments(this.soundbank);
        this.synthesizer.loadAllInstruments(soundbank);
        
        if (running)
        {
            play(this.currentMidiFile);
        }
    }
    
    @Override
    public void closeMidiPlayer()
    {
        stop();
        this.synthesizer.unloadAllInstruments(this.soundbank);
        this.synthesizer.close();
        this.sequencer.getTransmitters().forEach(action -> action.getReceiver().close());
        this.sequencer.getTransmitters().forEach(action -> action.close());
        this.sequencer.close();
    }
    
    @Override
    public void play(Path midiFile)
    {
        this.currentMidiFile = midiFile;
        try
        {
            if (tickPosition == 0)
            {
                sequencer.setSequence(MidiSystem.getSequence(this.currentMidiFile.toFile()));
            }
            else
            {
                sequencer.setTickPosition(tickPosition);
            }
            sequencer.start();
        }
        catch (InvalidMidiDataException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void stop()
    {
        tickPosition = 0;
        sequencer.stop();
    }
    
    @Override
    public void pause()
    {
        tickPosition = sequencer.getTickPosition();
        sequencer.stop();
    }
}
