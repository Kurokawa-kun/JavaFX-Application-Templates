package io.github.kurokawa_kun.javafx.templates.services;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.beans.property.*;
import lombok.*;
import org.springframework.stereotype.Service;

/**
*    サービスの起動処理とその監視を行うクラス
*/
@RequiredArgsConstructor
@Service
public class InitializationManagerImpl implements InitializationManager 
{
    @Getter
    private final List<Task> services = new ArrayList<Task>();
    private Runnable onLoadCompleted;
    
    //  TODO:起動させたいサービスをここに追加
    private final HtmlColorLoader htmlColorService;
    private final DummyService dummyService;
    
    @Getter 
    private final StringProperty messageProperty = new SimpleStringProperty();
    @Getter 
    private final DoubleProperty progressProperty = new SimpleDoubleProperty();
    private final DoubleProperty currentProgressProperty = new SimpleDoubleProperty();
    int totalProgress;
 
    @Override
    public void startLoading()
    {
    //  TODO:起動させたいサービスをここに追加
        services.add(htmlColorService);
        services.add(dummyService);
        
        ExecutorService startUpExecutorMaster = Executors.newSingleThreadExecutor();
        startUpExecutorMaster.submit(this); //  ここはJavaFXのスレッドで実行されるためブロックしないこと
        startUpExecutorMaster.shutdown();
    }
    
    /**
     *   サービスの起動処理を順番に呼び出す
     */
    @Override
    public void run()
    {
        if (services.isEmpty())
        {
            //  起動するサービスが何もないとき
            onLoadCompleted.run();
        }
        
        //  現在の進捗状況を表示する処理を登録する
        currentProgressProperty.addListener(cl ->
        {
            progressProperty.set(((double)totalProgress + currentProgressProperty.get()) / (double)services.size());
        });
        
        //  起動対象のサービスを順番に呼び出す
        for (totalProgress = 0 ; totalProgress < services.size(); totalProgress++)
        {
            ExecutorService startUpExecutor = Executors.newSingleThreadExecutor();            
            Task task = services.get(totalProgress);  //  次に初期化するサービス
            
            //  進捗状況が更新された際、通知を受け取る
            Platform.runLater(() ->
            {
                messageProperty.bind(task.messageProperty());
                currentProgressProperty.bind(task.progressProperty());
            });
            
            Future future = startUpExecutor.submit(task);
            try
            {
                future.get();   //  終了するまで待つ
            }
            catch (InterruptedException | ExecutionException e)
            {
                e.printStackTrace();
            }
            startUpExecutor.shutdown();
        }
        
        //  すべての初期化が完了したら登録されたコールバックを実行する
        onLoadCompleted.run();
    }
    
    /**
     *   ロードが完了したときに呼び出される処理を登録する
     *   @param callback 呼び出される処理
     */
    public void setOnLoadingCompleted(Runnable callback)
    {
        this.onLoadCompleted = callback;
    }
    
    public double getProgress()
    {
        return this.progressProperty.get();
    }
    public void setProgress(double p)
    {
        this.progressProperty.set(p);
    }
    public DoubleProperty progressProperty()
    {
        return progressProperty;
    }
    
    public StringProperty messageProperty()
    {
        return messageProperty;
    }
}
