package io.github.kurokawa_kun.javafx.templates.services;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

//  スリープするだけのサービス。PLAYING状態からINTERVAL状態になったときにこのサービスが開始される。
public class IntervalTimer extends Service<Void> 
{
    @Override
    protected Task<Void> createTask() 
    {
        return new Task<>() 
        {
            @Override
            protected Void call() throws Exception 
            {
                //  2秒間スリープ
                Thread.sleep(2000);
                return null;
            }
        };
    }
}
