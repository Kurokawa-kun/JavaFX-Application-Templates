package io.github.kurokawa_kun.javafx.templates.repositories;
import java.nio.file.Path;
import java.util.List;

public interface MidiFileRepository 
{
    /**
     * 指定されたパス配下の.midファイルを返却する
     *
     * @param path パス
     * @return ファイルのリスト
     */
    public List<Path> loadFiles(Path path);
}
