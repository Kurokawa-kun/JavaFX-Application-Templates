package io.github.kurokawa_kun.javafx.templates.services;
import javafx.beans.property.*;

/**
*    サービスの起動処理とその監視を行うクラスのインターフェース
*/
public interface InitializationManager extends Runnable
{
    /**
    *    サービスの起動
    */
    public void startLoading();
    
    /**
     *   サービスの起動処理を順番に呼び出す
     */
    @Override
    public void run();
    public void setOnLoadingCompleted(Runnable callback);
    public StringProperty messageProperty();    
    public double getProgress();
    public void setProgress(double p);
    public DoubleProperty progressProperty();
}
