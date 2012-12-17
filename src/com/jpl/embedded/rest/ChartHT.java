/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jpl.embedded.rest;

import arduinofx.model.MonitoringServiceStub;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jpl.embedded.model.BeanHT;
import com.jpl.embedded.model.ColBeansHT;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.io.IOUtils;

/**
 * Connects to http://<IP>:<PORT>/embedded/list and reads the measured values within two
 * dates and the maximun size specified, from response in json array format, 
 * deserializing it to a ColBeanHT object.
 * 
 * @author José Pereda Llamas
 * Created on 08-dic-2012 - 13:28:59
 */
public class ChartHT implements Runnable {

    private long calIni;
    private long calFin;
    private String tam;
    
    public ChartHT(long ini, long fin, String tam){
        this.calIni=ini;
        this.calFin=fin;
        this.tam=tam;
    }
    
    @Override
    public void run() {
        /*
         * REST request
         */
        URL theJsonUrl=null;
        try {
            theJsonUrl = new URL(MonitoringServiceStub.rutaServer+"/embedded/list?tam="+tam+"&ini="+calIni+"&end="+calFin);            
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
        ColBeansHT beans=null;
        try{
            beans=new Gson().fromJson(jSonTxt,ColBeansHT.class);
        } catch(JsonSyntaxException jse){
            System.out.println("Error gson: "+jse.getMessage());
            MonitoringServiceStub.getInstance().setError(true);
            return;
        }
        /*
         * Store ArrayList<BeanHT> as chartMeasure ArrayList<MeasureStub>
         */
        if(beans!=null){
            System.out.println("Size: "+beans.getCol().size());
            MonitoringServiceStub.getInstance().resetChartMeasures();
            for(BeanHT d:beans.getCol()){
                MonitoringServiceStub.getInstance().addToChartMeasures(d.getTemp(), d.getHum(), d.getTime());
            }            
            MonitoringServiceStub.getInstance().setError(false);
        }
        else{
            System.out.println("Error json");     
            MonitoringServiceStub.getInstance().setError(true);
        }       
        
    }
}
