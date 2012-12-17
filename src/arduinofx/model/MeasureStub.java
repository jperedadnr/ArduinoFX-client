package arduinofx.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author José Pereda Llamas
 * Created on 10-dic-2012 - 18:16:25
 */
public class MeasureStub implements IObservableMeasure {
    
    private final SimpleDoubleProperty temp;
    private final SimpleDoubleProperty hum;
    private final SimpleStringProperty time;
    private final SimpleBooleanProperty error;
    
    public MeasureStub(double temp, double hum, String time){
        this.temp=new SimpleDoubleProperty(temp);
        this.hum=new SimpleDoubleProperty(hum);
        this.time=new SimpleStringProperty(time);
        
        this.error=new SimpleBooleanProperty(false);
    }

    @Override
    public ObservableValue<Number> tempProperty() {
        return temp;
    }

    @Override
    public ObservableValue<Number> humProperty() {
        return hum;
    }

    @Override
    public ObservableValue<String> timeProperty() {
        return time;
    }

    @Override
    public ObservableValue<Boolean> errorProperty() {
        return error;
    }

    @Override
    public double getTemp() {
        return temp.get();
    }
    
    public void setTemp(double temp){
        this.temp.set(temp);
    }

    @Override
    public double getHum() {
        return hum.get();
    }
    
    public void setHum(double hum){
        this.hum.set(hum);
    }

    @Override
    public String getTime() {
        return time.get();
    }
    
    public void setTime(String time){
        this.time.set(time);
    }

    @Override
    public boolean isError() {
        return error.get();
    }
    
    public void setError(boolean error){
        this.error.set(error);
    }

    @Override
    public String toString() {
        return "ModelStub{" + "temp=" + temp.get() + ", hum=" + hum.get() + ", time=" + time.get() + ", error=" + error.get() + '}';
    }
    
    
}
