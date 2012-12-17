package arduinofx;

import arduinofx.model.IObservableMeasure;
import arduinofx.model.MonitoringServiceStub;
import com.jpl.embedded.rest.ChartHT;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import jfxtras.labs.scene.control.CalendarTextField;
import jfxtras.labs.scene.control.gauge.Content;
import jfxtras.labs.scene.control.gauge.Content.Align;
import jfxtras.labs.scene.control.gauge.Content.Effect;
import jfxtras.labs.scene.control.gauge.Content.Gap;
import jfxtras.labs.scene.control.gauge.Content.MatrixColor;
import jfxtras.labs.scene.control.gauge.Content.MatrixFont;
import jfxtras.labs.scene.control.gauge.Content.PostEffect;
import jfxtras.labs.scene.control.gauge.Content.RotationOrder;
import jfxtras.labs.scene.control.gauge.Content.Type;
import jfxtras.labs.scene.control.gauge.ContentBuilder;
import jfxtras.labs.scene.control.gauge.Gauge.FrameDesign;
import jfxtras.labs.scene.control.gauge.MatrixPanel;
import jfxtras.labs.scene.control.gauge.MatrixPanelBuilder;
import jfxtras.labs.scene.control.gauge.SimpleIndicator;
import jfxtras.labs.scene.control.gauge.SimpleIndicatorBuilder;

/**
 * ArduinoController create the custom controls, add the bindings to values from 
 * measures read from the server, and add the service required to get the chartMeasures
 * from the servlet
 * 
 * As the content comes from a regular Java Thread, Platform.runLater must be used to
 * update the content of any control
 * 
 * @author José Pereda Llamas
 * Created on 09-dic-2012 - 19:05:50
 */
public class ArduinoController implements Initializable {
    
    @FXML
    private VBox vbox;
    @FXML
    private HBox hbox;
    @FXML
    private HBox hbox2;
    @FXML
    private Label lblTime;
    @FXML
    private Button btnEvolution;
    @FXML
    private HBox hboxStatus;
    @FXML
    private Label lblStatus;
    @FXML
    private ProgressIndicator progress;
    
    private final DoubleProperty dTemp=new SimpleDoubleProperty(-1d);        
    private final DoubleProperty dHum=new SimpleDoubleProperty(-1d);
    private final BooleanProperty bChange=new SimpleBooleanProperty(false);
    
    private  Service<Void> chartService = null;
    
    private final ChoiceBox sizeChoiceBox = new ChoiceBox(FXCollections.observableArrayList(
                                        Arrays.asList("50","100","500","1000","5000")));
    
    /*
     * Custom controls from http://jfxtras.org
     * -MatrixPanel
     * -CalendarTextField
     * -SimpleIndicator
     */
    private final MatrixPanel animatedPanel = 
          MatrixPanelBuilder.create()
                            .ledWidth(192).ledHeight(18)
                            .prefWidth(650.0).prefHeight(400.0)
                            .frameDesign(FrameDesign.DARK_GLOSSY)
                            .frameVisible(true)
                            .build();
    
    private final CalendarTextField lStartCalendarTextField = new CalendarTextField().withShowTime(true);
    private final CalendarTextField lEndCalendarTextField = new CalendarTextField().withShowTime(true);
    
    
    private final SimpleIndicator indicator = SimpleIndicatorBuilder.create()
                                                .prefHeight(40).prefWidth(40)
                                                .innerColor(Color.rgb(0,180,0).brighter())
                                                .outerColor(Color.rgb(0,180,0).darker())
                                                .build();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        /*
         * 1. MatrixPanel and lastMeasure related
         */
        vbox.getChildren().add(0,animatedPanel);
        
        dTemp.bind(MonitoringServiceStub.getInstance().getLastMeasure().tempProperty());
        dHum.bind(MonitoringServiceStub.getInstance().getLastMeasure().humProperty());
        
        dTemp.addListener(new ChangeListener<Number>(){

            @Override 
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                // System.out.println("T changed:"+t1.doubleValue()+" <- "+t.doubleValue());
                bChange.set(true);
            }
            
        });
        
        dHum.addListener(new ChangeListener<Number>(){

            @Override 
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                // System.out.println("H changed:"+t1.doubleValue()+" <- "+t.doubleValue());
                bChange.set(true);
            }
            
        });
        
        /*
         * MatrixPanel Content is updated if dTemp and/or dHum have changed, just once
         */
        bChange.addListener(new ChangeListener<Boolean>(){

            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                
                if(t1.booleanValue()){
                    /*
                     * if new T,H values, change content of the MatrixPanel in a JavaFX thread
                     */
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
//                            System.out.println("Updating MatrixPanel Content");
                            Content contentTemp = ContentBuilder.create()
                                            .color(MatrixColor.GREEN)
                                            .type(Type.TEXT)
                                            .txtContent("Temperature: " + String.format("%.1f",MonitoringServiceStub.getInstance().getLastMeasure().getTemp()) + " ºC")
                                            .font(MatrixFont.FF_8x16).fontGap(Gap.SIMPLE)
                                            .origin(0, 1).area(0, 0, 191, 18)
                                            .align(Align.RIGHT).effect(Effect.SCROLL_LEFT)
                                            .lapse(20).postEffect(PostEffect.PAUSE)
                                            .pause(3000).order(RotationOrder.FIRST)
                                            .build();
                            Content contentHum =  ContentBuilder.create()
                                            .color(MatrixColor.GREEN)
                                            .type(Type.TEXT)
                                            .txtContent("Humidity: " + String.format("%.1f",MonitoringServiceStub.getInstance().getLastMeasure().getHum()) + " %")
                                            .font(MatrixFont.FF_8x16).fontGap(Gap.SIMPLE)
                                            .origin(0, 1).area(0, 0, 191, 18)
                                            .align(Align.RIGHT).effect(Effect.SCROLL_LEFT)
                                            .lapse(20).postEffect(PostEffect.PAUSE).pause(3000)
                                            .order(RotationOrder.SECOND)
                                            .build();

                            /*
                             * Set MatrixPanel new content
                             */
                            animatedPanel.setContents(Arrays.asList(contentTemp, contentHum));
                            
                            bChange.set(false);
                        }
                    });
                }
            }
        });
        
        /*
         * 2. Label of last reading related
         */
        StringProperty sTim=new SimpleStringProperty("");
        sTim.bind(MonitoringServiceStub.getInstance().getLastMeasure().timeProperty());
        sTim.addListener(new ChangeListener<String>(){

            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                /*
                 * Update label with last reading time, in a JavaFX thread
                 */
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        lblTime.setText("Last Reading Time: "+MonitoringServiceStub.getInstance().getLastMeasure().getTime());
                    }
                });
            }
        });
        
        
        /*
         * 3. Chart related
         */
        progress.setVisible(false);
        
        Calendar calEnd=Calendar.getInstance();
        calEnd.set(calEnd.get(Calendar.YEAR), calEnd.get(Calendar.MONTH), calEnd.get(Calendar.DAY_OF_MONTH), calEnd.get(Calendar.HOUR_OF_DAY), calEnd.get(Calendar.MINUTE), 0);
        
        lEndCalendarTextField.setValue(calEnd);
        lEndCalendarTextField.setPrefWidth(180);
        Calendar calIni=Calendar.getInstance();
        calIni.set(calIni.get(Calendar.YEAR), calIni.get(Calendar.MONTH), calIni.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        
        lStartCalendarTextField.setValue(calIni);
        lStartCalendarTextField.setPrefWidth(180);
        
        hbox.getChildren().add(lStartCalendarTextField);
        hbox.getChildren().add(lEndCalendarTextField);
        
        sizeChoiceBox.getSelectionModel().selectFirst();
        hbox.getChildren().add(sizeChoiceBox);
        
        
        /*
         * Service to call rest list service, without blocking the application
         */
        chartService=new Service<Void>(){

            @Override
            protected Task<Void> createTask() {
                
                return new Task<Void>(){

                    @Override
                    protected Void call() throws Exception {
                            
                        ChartHT ht=new ChartHT(lStartCalendarTextField.getValue().getTimeInMillis(), 
                                               lEndCalendarTextField.getValue().getTimeInMillis(),
                                               sizeChoiceBox.getSelectionModel().getSelectedItem().toString());
                        /*
                         * when the request is performed, it takes some time (even a minute)
                         * to finish reading the values from the server
                         */
                        ht.run();
                        
                        if (MonitoringServiceStub.getInstance().getLastMeasure().isError()) {
                            throw new RuntimeException("Historic Error");
                        }
                        return null;
                    }                    
                };
            }
        };
        
        /*
         * listen to service state. 
         * 
         * This is the way to know when ChartHT has finished, if State is State.SUCCEEDED. 
         * 
         * Then, with a JavaFX thread, a chart is created an the the data is plotted.
         */
        chartService.stateProperty().addListener(new ChangeListener<State>(){

            @Override
            public void changed(ObservableValue<? extends State> ov, State t, State t1) {
                if(chartService.getException()!=null){
                    System.out.println(chartService.getException().getMessage()); 
                    btnEvolution.setDisable(false);
                } else if(t1==State.SUCCEEDED) {
                    /*
                     * JavaFX thread to plot the chartMeasures
                     */
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {

                            final CategoryAxis xAxis = new CategoryAxis();
                            final NumberAxis yAxis = new NumberAxis();
                            
                            //creating the chart
                            final LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);

                            lineChart.getStyleClass().add("custom-chart");
                            lineChart.setCreateSymbols(false);
                            lineChart.setPrefSize(600, 500);
                            lineChart.setTitle("Evolution of Temperature & Humidity");
                            //defining a series
                            XYChart.Series series1 = new XYChart.Series();
                            series1.setName("Temperature (ºC)");
                            XYChart.Series series2 = new XYChart.Series();
                            series2.setName("Relative Humidity (%)");
                            //populating the series with data
                            for(IObservableMeasure d:MonitoringServiceStub.getInstance().getChartMeasures()){
                                series1.getData().add(new XYChart.Data(d.getTime(),d.getTemp()));
                                series2.getData().add(new XYChart.Data(d.getTime(),d.getHum()));
                            }

                            lineChart.getData().addAll(series1,series2);
                            progress.setVisible(false);        
                            hbox2.getChildren().add(1,lineChart);
                            
                            btnEvolution.setDisable(false);
                        }
                    });
                }                
            }
        });
        
        /*
         * 4. SimpleIndicator and status label related
         */
        hboxStatus.getChildren().add(0, indicator);
        lblStatus.setText("Connection established to "+MonitoringServiceStub.rutaServer);
        btnEvolution.setDisable(false);
        
        BooleanProperty error=new SimpleBooleanProperty(false);
        error.bind(MonitoringServiceStub.getInstance().getLastMeasure().errorProperty());
        error.addListener(new ChangeListener<Boolean>(){

            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, final Boolean t1) {
                /*
                 * State of Error or success in connecting to server is updated in a JavaFX thread
                 */
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(!t1.booleanValue()){
                            indicator.setInnerColor(Color.rgb(0,180,0).brighter());
                            indicator.setOuterColor(Color.rgb(0,180,0).darker());
                            lblStatus.setText("Connection established to "+MonitoringServiceStub.rutaServer);
                            btnEvolution.setDisable(false);
                        } else {
                            indicator.setInnerColor(Color.rgb(180,0,0).brighter());
                            indicator.setOuterColor(Color.rgb(180,0,0).darker());
                            lblStatus.setText("Connection failed");
                            btnEvolution.setDisable(true);
                        }
                    }
                });
            }
            
        });
        
    }  
    
    @FXML
    public void getEvolution(ActionEvent a){
        
        btnEvolution.setDisable(true);
        if(hbox2.getChildren().size()>1){
            hbox2.getChildren().remove(1);
        }
        progress.setVisible(true);
        /*
         * Call to service
         */
        chartService.restart(); 
        
    }
    
}
