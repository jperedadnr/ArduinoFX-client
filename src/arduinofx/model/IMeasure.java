package arduinofx.model;

/**
 *
 * @author José Pereda Llamas
 * Created on 08-dic-2012 - 13:32:55
 */
public interface IMeasure {
    public double getTemp();
    public double getHum();
    public String getTime();
    
    public boolean isError();
}
