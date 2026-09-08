package io.github.kurokawa_kun.javafx.templates.models;
import io.github.kurokawa_kun.javafx.templates.entities.AppMode;
import java.nio.file.Path;
import java.util.List;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;

public interface MainModel
{
    public static final byte[] GM_RESET = {(byte)0xF0, (byte)0x7E, (byte)0x7F, (byte)0x09, (byte)0x01, (byte)0xF7};
    public static final byte[] GM2_RESET = {(byte)0xF0, (byte)0x7E, (byte)0x7F, (byte)0x09, (byte)0x03, (byte)0xF7};
    public static final byte[] GS_RESET = {(byte)0xF0, (byte)0x41, (byte)0x10, (byte)0x42, (byte)0x12, (byte)0x40, (byte)0x00, (byte)0x7F, (byte)0x00, (byte)0x41, (byte)0xF7};
    public static final byte[] GS_SYSTEM_MODE1_SET = {(byte)0xF0, (byte)0x41, (byte)0x10, (byte)0x42, (byte)0x12, (byte)0x00, (byte)0x00, (byte)0x7F, (byte)0x00, (byte)0x01, (byte)0xF7};
    public static final byte[] GS_SYSTEM_MODE2_SET = {(byte)0xF0, (byte)0x41, (byte)0x10, (byte)0x42, (byte)0x12, (byte)0x00, (byte)0x00, (byte)0x7F, (byte)0x01, (byte)0x00, (byte)0xF7};
    public static final byte[] XG_SYSTEM_ON = {(byte)0xF0, (byte)0x43, (byte)0x10, (byte)0x4C, (byte)0x00, (byte)0x00, (byte)0x7E, (byte)0x00, (byte)0xF7};    
    
    public static final Color BACKGROUND_COLOR_GM = Color.ALICEBLUE;
    public static final Color BACKGROUND_COLOR_GS = Color.DARKORANGE;
    public static final Color BACKGROUND_COLOR_XG = Color.LAWNGREEN;    
    
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
     *   プレイヤーが保持しているシーケンサーを閉じる
     */
    public void close();
    
    /**
     *   現在のファイル名を取得する
     *   @return ファイル名
     */
    public String getFileName();
    
    /**
     *   現在のファイル名を格納したプロパティを取得する
     *   @return ファイル名を格納したプロパティ
     */
    public StringProperty fileNameProperty();
    
    /**
     *   現在のタイトルを取得する
     *   @return タイトル
     */
    public String getTitle();
    
    /**
     *   現在のタイトルを格納したプロパティを取得する
     *   @return タイトルを格納したプロパティ
     */
    public StringProperty titleProperty();
        
    /**
     *   現在の再生番号を取得する
     *   @return 再生番号
     */
    public int getPos();
    
    /**
     *   再生リストを計算する
     *   @return 再生リスト
     */
    public List<Path> getFilelist();
    
    /**
     *   現在の背景色を取得する
     *   @return 背景色
     */
    public Color getBackgroundColor();
    /**
     *   現在の背景色のプロパティを取得する
     *   @return 背景色のプロパティ
     */
    public ObjectProperty<Color> backgroundColorProperty();
    
    /**
     *   サウンドフォントのロード
     *   @param path sf2ファイルのパス
     */
    public void loadSoundFont(Path path);
    
    /**
     *   アプリの現在のモードを取得する
     *   @return 現在のモード
     */
    public AppMode getAppMode();    

    /**
     *   アプリの現在のモードを格納したプロパティを取得する
     *   @return 現在のモードを格納したプロパティ
     */
    public ObjectProperty<AppMode> appModeProperty();
}
