package io.github.kurokawa_kun.javafx.templates.services;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.*;

//  画面の色を変えるだけのサービス
public class ColorChanger extends Service<Void>
{
    private static final Duration ANIMATION_DURATION = Duration.seconds(2);    
    @Getter @Setter
    private Color currentColor;
    @Getter @Setter
    private ObjectProperty<Color> targetProperty = null;   //  変更対象のプロパティへの参照
    @Getter @Setter
    private Color targetColor;
    
    /**
     *   コンストラクタ
     *   @param targetProperty オブジェクトプロパティへの参照。このプロパティの色を直接変更する
     */    
    public ColorChanger(ObjectProperty<Color> targetProperty)
    {
        this.targetProperty = targetProperty;
    }
    
    @Override
    protected Task<Void> createTask() 
    {
        return new Task<>() 
        {
            @Override
            protected Void call() throws Exception 
            {
                if (getTargetProperty()== null)
                {
                    return null;
                }
                
                //  タイムラインを構築する
                Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(getTargetProperty(), currentColor)),
                    new KeyFrame(ANIMATION_DURATION, new KeyValue(getTargetProperty(), targetColor))
                );
                
                //  アニメーションの実行
                timeline.play();

                return null;
            }
        };
    }
}
