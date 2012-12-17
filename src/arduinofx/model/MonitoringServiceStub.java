package arduinofx.model;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author José Pereda Llamas
 * Created on 08-dic-2012 - 13:32:55
 */
public class MonitoringServiceStub implements MonitoringService {
    
    /*
     * SET YOUR SERVER IP AND PORT HERE
     */
    public static final String urlServer="http://192.168.0.39:8080";  
    
    private IObservableMeasure lastMeasure=null;
    private ArrayList<IObservableMeasure> chartMeasures=null;
    
    private static MonitoringServiceStub instance = new MonitoringServiceStub();
    
    private MonitoringServiceStub(){
        lastMeasure=new MeasureStub(0d,0d,Calendar.getInstance().getTime().toString());
        chartMeasures=new ArrayList<IObservableMeasure>();
    }
    
    public static MonitoringServiceStub getInstance() { return instance; }

    /*
     * MATRIXPANEL
     */
    @Override
    public void setLastMeasure(double temp, double hum, String time) {
        ((MeasureStub)lastMeasure).setTemp(temp);
        ((MeasureStub)lastMeasure).setHum(hum);
        ((MeasureStub)lastMeasure).setTime(convertTime(time));
    }
    
    @Override
    public void setError(boolean error){
        ((MeasureStub)lastMeasure).setError(error);
    }
    
    @Override
    public IObservableMeasure getLastMeasure(){
        return lastMeasure;
    }
    
    /*
     * CHART 
     */
    @Override
    public void resetChartMeasures(){
        chartMeasures.clear();
    }
    
    @Override
    public void addToChartMeasures(double temp, double hum, String time) {
        MeasureStub stub = new MeasureStub(temp,hum,convertTime(time));
        chartMeasures.add(stub);
    }
    
    @Override
    public ArrayList<IObservableMeasure> getChartMeasures(){
        return chartMeasures;
    }
    
    /*
     * Converts Calendar.getTime().toString from ISO 8601 format to SimpleDateFormat
     */
    private String convertTime(String time) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            cal.setTime(sdf1.parse(time));
        } catch (ParseException ex1) {
            try {
                cal.setTime(sdf2.parse(time));
            } catch (ParseException ex2) {
                try {
                    cal.setTime(sdf3.parse(time));
                } catch (ParseException ex3) {
                    System.out.println("Error "+ex3);
                }
            }
        }  
        
        Format format=new SimpleDateFormat("dd/MM/yy HH:mm:ss");                            
        return format.format(cal.getTime());
    } 
    
}
