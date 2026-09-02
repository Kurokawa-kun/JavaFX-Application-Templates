package io.github.kurokawa_kun.javafx.templates.repositories;
import java.util.Map;
import javafx.scene.paint.Color;

/**
 *   HTMLカラー（webカラー）を保持しているリポジトリ
 */
public interface HtmlColorRepository
{
    /**
     *   HTMLカラーを格納したマップを設定する
     * @param htmlColor HTMLカラー
     */
    public void setHtmlColor(Map<String, Color> htmlColor);
    /**
     *   HTMLカラーを格納したマップを取得する
     * @return HTMLカラー
     */
    public Map<String, Color> getHtmlColor();
}
