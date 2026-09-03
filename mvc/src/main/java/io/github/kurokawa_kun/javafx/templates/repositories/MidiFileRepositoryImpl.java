package io.github.kurokawa_kun.javafx.templates.repositories;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class MidiFileRepositoryImpl implements MidiFileRepository
{
    @Override
    public List<Path> loadFiles(Path path)
    {
        List<Path> collectedPaths = new ArrayList<>();

        //  再帰的にファイルとディレクトリを収集
        try
        {
            Files.walkFileTree(path, new SimpleFileVisitor<>() 
            {
                //  ファイルが見つかったときの処理
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes basicFileAttributes)
                {
                    String fileName = file.getFileName().toString().toLowerCase();
                    // *.mid ファイルのみ対象（大文字小文字を区別しない）
                    if (fileName.endsWith(".mid")) 
                    {
                        collectedPaths.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                //  ディレクトリに入る前の処理
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes basicFileAttributes)
                {
                    //  ルートディレクトリは除く
                    if (!dir.equals(path)) 
                    {
                        collectedPaths.add(dir);
                    }
                    return FileVisitResult.CONTINUE;
                }

                //  エラーが発生した場合はスキップして探索を継続
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exception) 
                {
                    return FileVisitResult.CONTINUE;
                }
            });            
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        //  ディレクトリを除外する
        collectedPaths.removeIf(Files::isDirectory);
        
        //  グループ順にソート
        //  同一グループ内は名前順にソート
        collectedPaths.sort(Comparator.comparingInt(MidiFileRepositoryImpl::getGroup).thenComparing(other -> other.getFileName().toString(), String.CASE_INSENSITIVE_ORDER));

        return collectedPaths;
    }

    //  グループ番号を返却する
    private static int getGroup(Path path) 
    {
        /*
            グループ1：テンプレートファイル
            グループ2：イントロ
            グループ3：それ以外のファイル
            グループ4：エンディング
            グループ5：ディレクトリ        
        */        
        if (Files.isDirectory(path)) 
        {
            return 5;
        }

        String fileName = path.getFileName().toString();
        if (fileName.startsWith("TMP_") || fileName.startsWith("GM_Reset") || fileName.startsWith("00_") || fileName.startsWith("tmp1") || fileName.startsWith("tmp2")) 
        {
            return 1;
        }
        if (fileName.startsWith("IN") || fileName.startsWith("Intro")) 
        {
            return 2;
        }        
        if (fileName.startsWith("EN") || fileName.startsWith("Ending"))
        {
            return 4;
        }        
        return 3;
    }
}
