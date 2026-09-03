package io.github.kurokawa_kun.javafx.templates.repositories;
import java.io.IOException;
import java.nio.file.Path;
import javax.sound.midi.*;

public class SoundFontRepositoryImpl implements SoundFontRepository
{
    @Override
    public Soundbank getSoundFont(Path path)
    {
        Soundbank soundbank = null;
        
        try
        {
            soundbank = MidiSystem.getSoundbank(path.toFile());
        }
        catch (InvalidMidiDataException | IOException e)
        {
            e.printStackTrace();
        }
        
        return soundbank;
    }
}
