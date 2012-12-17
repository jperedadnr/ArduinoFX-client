package arduinofx.model;

import javafx.beans.value.ObservableValue;

/**
 *
 * @author José Pereda Llamas
 * Created on 08-dic-2012 - 13:32:55
 */
public interface IObservableMeasure extends IMeasure {
    public ObservableValue<Number> tempProperty();  
    public ObservableValue<Number> humProperty();  
    public ObservableValue<String> timeProperty();
    public ObservableValue<Boolean> errorProperty();
}
