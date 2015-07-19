package gamefx001;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import java.util.Random;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Ajith Kp (ajithkp560)
 * http://www.terminalcoders.blogspot.de
 * (c) _TERMINAL_CODERS_
 * 
 */
public class AirWar extends Application {
    int x = 0;
    IntegerProperty scoreCntr = new SimpleIntegerProperty(this, "scoreCntr");
    VBox vx;
    @Override
    public void start(Stage stage)  {
        Image bg = new Image(this.getClass().getResource("bg.jpg").toString());
        Image plri = new Image(this.getClass().getResource("player.png").toString());
        Image enmi = new Image(this.getClass().getResource("enemy.png").toString());
        ImageView back = new ImageView(bg);
        ImageView plr = new ImageView(plri);
        plr.setTranslateY(630);
        plr.setTranslateX(200);
        ImageView enm0 = new ImageView(enmi);
        enm0.setTranslateY(5);
        enm0.setTranslateX(200);
        Timeline line = new Timeline();
        EventHandler<ActionEvent> onFinished = (ActionEvent t) -> {
            changeProps(enm0);
        };
        line.setCycleCount(Timeline.INDEFINITE);
        final KeyValue kv = new KeyValue(enm0.yProperty(), 700);
        final KeyFrame kf = new KeyFrame(Duration.millis(3000), onFinished,kv);
        line.getKeyFrames().add(kf);
        line.play();
        
        Text scoreTxt = new Text();
        scoreTxt.setFont(Font.font("Arial", 20.0D));
        scoreTxt.textProperty().bind(Bindings.concat(new Object[] { "Hits: ", this.scoreCntr }));
        vx = ((VBoxBuilder)((VBoxBuilder)((VBoxBuilder)VBoxBuilder.create().children(new Node[] { scoreTxt })).translateX(20.0D)).translateY(20.0D)).build();
        
        AnchorPane root = new AnchorPane();
        root.getChildren().add(back);
        root.getChildren().add(plr);
        root.getChildren().add(enm0);
        root.getChildren().add(vx);
        Scene scene = new Scene(root, 500, 700);
        scene.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode()==KeyCode.RIGHT) {
                movePlayer(plr, 10);
            } else if (event.getCode()==KeyCode.LEFT) {
                movePlayer(plr, -10);
            } else if (event.getCode()==KeyCode.SPACE) {
                Circle c = new Circle(5, Color.RED);
                c.setCenterX(200+75/2+plr.getX());
                c.setCenterY(630);
                Timeline ct = new Timeline();
                ChangeListener<Number> checkFire = (ob, n, n1)->{
                    if(c.getBoundsInParent().intersects(enm0.getBoundsInParent()))
                    {
                        line.stop();
                        changeProps(enm0);
                        line.playFromStart();
                        ct.stop();
                        c.setCenterY(-5);
                        scoreCntr.set(scoreCntr.get()+1);
                        System.out.println("Enemy Destroyed");
                    }
                };
                enm0.translateYProperty().addListener(checkFire);
                enm0.translateXProperty().addListener(checkFire);
                c.centerYProperty().addListener(checkFire);
                c.centerXProperty().addListener(checkFire);
                EventHandler<ActionEvent> onFinished1 = (ActionEvent t) -> {
                    root.getChildren().remove(c);
                };
                final KeyValue kv1 = new KeyValue(c.centerYProperty(), -10);
                final KeyFrame kf1 = new KeyFrame(Duration.millis(1000), onFinished1, kv1);
                ct.getKeyFrames().add(kf1);
                ct.play();
                root.getChildren().add(c);
            }
        });
        stage.setTitle("My Game");
        stage.setScene(scene);
        stage.show();
    }
    void movePlayer(ImageView plr, int v)
    {  
            x+=v;
            Timeline move = new Timeline();
            EventHandler<ActionEvent> onFinished = (ActionEvent t) -> {
                if(plr.getX()<-200)
                {
                    move.stop();
                    plr.setX(-200);
                    x = -200;
                }
                else if(plr.getX()>220)
                {
                    plr.setX(220);
                    x = 220;
                    move.stop();
                }
                return;
            };
            final KeyValue kv = new KeyValue(plr.xProperty(), x);
            final KeyFrame kf = new KeyFrame(Duration.millis(500), onFinished, kv);
            move.getKeyFrames().add(kf);    
            move.play();
    }
    public static void main(String[] args) {
        launch(args);
    }

    private void changeProps(ImageView enm) {
        System.out.println("Property Changed");
        Random rand = new Random();
        int v = rand.nextInt(2);
        if(v==0)
        {
            enm.setX(-1*rand.nextInt(200));
        }
        else
        {
            enm.setX(rand.nextInt(200));
        }
        enm.setY(5);
    }
}