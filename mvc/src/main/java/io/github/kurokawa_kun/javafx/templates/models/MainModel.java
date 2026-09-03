package io.github.kurokawa_kun.javafx.templates.models;
import java.nio.file.Path;

public interface MainModel
{
    /**
     *   ボタンの有効/無効を変更する
     */
    public void setButtonDisability();
    
    /**
     *   プレイヤーが保持しているシーケンサーを閉じる
     */
    public void close();
    
    /**
     *   ディレクトリ配下のmidファイルを探す
     *   @param path 対象ディレクトリ
     */
    public void load(Path path);
    
    /**
     *   「前へ」ボタンが押されたときの処理
     */
    public void prev();
    
    /**
     *   「停止」ボタンが押されたときの処理
     */
    public void stop();
    
    /**
     *   「次へ」ボタンが押されたときの処理
     */
    public void next();
    
    /**
     *   「再生」「一時停止」ボタンが押されたときの処理
     */
    public void play();
    
    /**
     *   サウンドフォントのロード
     *   @param sf2ファイルのパス
     */
    public void loadSoundFont(Path path);
}
