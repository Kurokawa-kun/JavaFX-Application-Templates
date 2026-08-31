package io.github.kurokawa_kun.javafx.templates.services;
import java.io.IOException;
import java.nio.file.Path;
import javax.sound.midi.*;
import lombok.*;

//  MIDIプレイヤー
public class MidiPlayer
{    
    @Getter
    private Sequencer sequencer = null;
    private long tickPosition = 0;
    
    /**
     *   コンストラクタ
     */
    public MidiPlayer()
    {
        openSequencer();
    }
    
    /**
     *   コンストラクタ
     */
    public MidiPlayer(Soundbank soundbank)
    {
        Synthesizer synthesizer = null;
        try
        {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            
            //  サウンドフォントをサポートしているかチェック
            if (synthesizer.isSoundbankSupported(soundbank)) 
            {
                //  既存のデフォルト音源をアンロード
                Soundbank defaultSoundbank = synthesizer.getDefaultSoundbank();
                if (defaultSoundbank != null) 
                {
                    synthesizer.unloadAllInstruments(defaultSoundbank);
                }
                // 新しいサウンドフォントの音源をロード
                synthesizer.loadAllInstruments(soundbank);
            } 
            else 
            {
                System.err.println("このシステムのシンセサイザーはサウンドフォントをサポートしていません。");
                return;
            }

            //  シーケンサーの取得（送信ポートを自動接続しない設定で取得）
            //  ※引数をfalseにすることで標準音源ではなく独自設定したシンセサイザーに出力を繋ぐ
            if (sequencer != null && sequencer.isOpen())
            {
                sequencer.close();
            }
            sequencer = MidiSystem.getSequencer(false);
            sequencer.open();
            
            //  シンセサイザーとシーケンサーを接続
            sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
        }
        catch (MidiUnavailableException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     *   指定されたMIDIファイルを再生する
     *   @param MIDIファイルのパス
     */
    public void play(Path midiFile)
    {
        try
        {
            if (sequencer.isRunning())
            {
                sequencer.stop();
            }
            if (tickPosition == 0)
            {
                sequencer.setSequence(MidiSystem.getSequence(midiFile.toFile()));                
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
    
    /**
     *   MIDIファイルの再生を止める
     */
    public void stop()
    {
        tickPosition = 0;
        sequencer.stop();
    }
    
    /**
     *   MIDIファイルの再生を一時停止する
     */
    public void pause()
    {
        tickPosition = sequencer.getTickPosition();
        sequencer.stop();
    }
    
    /**
     *   シーケンサーを開く
     */
    public void openSequencer()
    {
        try
        {
            this.sequencer = MidiSystem.getSequencer();
            sequencer.open();
        }
        catch (MidiUnavailableException e)
        {
            e.printStackTrace();
        }
        
    }
    
    /**
     *   シーケンサーを閉じる
     */
    public void closeSequencer()
    {
        sequencer.close();
    }    
}
