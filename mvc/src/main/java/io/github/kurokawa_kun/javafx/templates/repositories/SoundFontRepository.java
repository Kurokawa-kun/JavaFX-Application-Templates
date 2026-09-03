package io.github.kurokawa_kun.javafx.templates.repositories;
import java.nio.file.Path;
import javax.sound.midi.Soundbank;

public interface SoundFontRepository
{
    /**
     *   サウンドフォントを返却する
     *   @param path sf2ファイルのパス
     *   @return サウンドフォント
     */
    public Soundbank getSoundFont(Path path);
}
