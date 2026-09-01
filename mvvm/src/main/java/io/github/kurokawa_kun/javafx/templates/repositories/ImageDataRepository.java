package io.github.kurokawa_kun.javafx.templates.repositories;
import io.github.kurokawa_kun.javafx.templates.entities.ImageData;
import java.nio.file.Path;

/**
 *   画像データを返却するリポジトリ
 */
public interface ImageDataRepository
{
    /**
     *   ImageData形式で画像データを返却する
     *   @param filePath 画像のパス
     *   @return 画像データ
     */
    public ImageData load(Path filePath);
}
