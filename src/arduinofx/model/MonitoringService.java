package arduinofx.model;

import java.util.ArrayList;

/**
 *
 * @author José Pereda Llamas
 * Created on 08-dic-2012 - 13:32:55
 */
public interface MonitoringService {
    public void setLastMeasure(double temp, double hum, String time);
    public IObservableMeasure getLastMeasure();
    public void setError(boolean error);
    
    public void resetChartMeasures();
    public void addToChartMeasures(double temp, double hum, String time);
    public ArrayList<IObservableMeasure> getChartMeasures();
}
