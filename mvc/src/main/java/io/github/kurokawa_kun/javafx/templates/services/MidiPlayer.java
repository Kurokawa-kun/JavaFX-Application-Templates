package io.github.kurokawa_kun.javafx.templates.services;
import java.nio.file.Path;
import javax.sound.midi.*;

//  MIDIプレイヤー
public interface MidiPlayer
{
    /**
     *   サウンドフォントを差し替える
     *   @param soundbank サウンドフォントのパス
     */
    public void setSoundbank(Soundbank soundbank);
    
    /**
     *   MIDIプレイヤーを閉じる
     */
    public void closeMidiPlayer();
    
    /**
     *   指定されたMIDIファイルを再生する
     *   @param midiFile MIDIファイルのパス
     */
    public void play(Path midiFile);
    
    /**
     *   MIDIファイルの再生を止める
     */
    public void stop();
    
    /**
     *   MIDIファイルの再生を一時停止する
     */
    public void pause();
}
