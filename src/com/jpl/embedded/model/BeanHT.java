package com.jpl.embedded.model;

/**
 *
 * @author José Pereda Llamas
 * Created on 08-dic-2012 - 13:20:36
 */
public class BeanHT {
    
    private double temp;
    private double hum;
    private String time;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getHum() {
        return hum;
    }

    public void setHum(double hum) {
        this.hum = hum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "BeanHT{" + "temp=" + temp + ", hum=" + hum + ", time=" + time + '}';
    }
    
}
