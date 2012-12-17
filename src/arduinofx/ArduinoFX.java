package arduinofx;

import com.jpl.embedded.rest.LastHT;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * start and stop Application overrided methods are used to start and stop
 * and scheduled task
 * 
 * @author José Pereda Llamas
 * Created on 09-dic-2012 - 19:05:50
 */
public class ArduinoFX extends Application {
    
    /*
     * Scheduled Task to read every 30 seconds from the server the last values measured
     */
    private long EVENT_CYCLE = 30000;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> scheduleAtFixedRate = null;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        scheduleAtFixedRate = scheduler.scheduleAtFixedRate(new LastHT(), 0, EVENT_CYCLE, TimeUnit.MILLISECONDS);
        
        Parent root = FXMLLoader.load(getClass().getResource("Arduino.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("ArduinoFX");
        stage.show();
    }

    @Override
    public void stop(){
        System.out.println("Stopping scheduler");                    
        scheduleAtFixedRate.cancel(true);
        scheduler.shutdown();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
