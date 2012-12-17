package com.jpl.embedded.rest;

import arduinofx.model.MonitoringServiceStub;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jpl.embedded.model.BeanHT;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.io.IOUtils;

/**
 * Connects to http://<IP>:<PORT>/embedded/last and reads the last measured values
 * of H and T from response in json format, deserializing it to a BeanHT object.
 * 
 * @author José Pereda Llamas
 * Created on 08-dic-2012 - 13:28:59
 */
public class LastHT implements Runnable {
    
    @Override
    public void run() {
        /*
         * REST request
         */
        URL theJsonUrl=null;
        try {
            theJsonUrl = new URL(MonitoringServiceStub.rutaServer+"/embedded/last");            
        } catch (MalformedURLException ex) {
            System.out.println("Error url: " + ex.getMessage());
            MonitoringServiceStub.getInstance().setError(true);
            return;
        }
        /*
         * Read JSON from url
         */
        System.out.println("Reading JSON object");
        String jSonTxt="";
        try {
            InputStream in=theJsonUrl.openStream();
            jSonTxt=IOUtils.toString(in);
        } catch (IOException ex) {
            System.out.println("Error reading url: " +theJsonUrl+" "+ ex.getMessage());
            MonitoringServiceStub.getInstance().setError(true);
            return;
        }
        /*
         * Deserialize JSON
         */
        BeanHT bean=null;
        try{
            bean=new Gson().fromJson(jSonTxt,BeanHT.class);
        } catch(JsonSyntaxException jse){
            System.out.println("Error gson: "+jse.getMessage());
            MonitoringServiceStub.getInstance().setError(true);
            return;
        }
        /*
         * Store BeanHT as lastMeasure MeasureStub
         */
        if(bean!=null){
            MonitoringServiceStub.getInstance().setLastMeasure(bean.getTemp(), bean.getHum(), bean.getTime()); 
            MonitoringServiceStub.getInstance().setError(false);
        }
        else{
            System.out.println("Error reading json");     
            MonitoringServiceStub.getInstance().setError(true);
        }       
        
    }
}
