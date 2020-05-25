package it.polito.tdp.rivers;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.rivers.model.Model;
import it.polito.tdp.rivers.model.River;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;
	private ObservableList<River> rivers = FXCollections.observableArrayList();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<River> boxRiver;

    @FXML
    private TextField txtStartDate;

    @FXML
    private TextField txtEndDate;

    @FXML
    private TextField txtNumMeasurements;

    @FXML
    private TextField txtAvgFlow;

    @FXML
    private TextField txtK;

    @FXML
    private TextArea txtResult;
    
    @FXML
    void completeFields(ActionEvent event) {

    	txtResult.clear();
    	
    	River r = this.boxRiver.getValue();
    	if(r==null)
    	{
    		txtResult.setText("Please select a river!\n");
    		return;
    	}
    	
    	model.getRiverInformations(r.getId());
    	txtStartDate.setText(r.getFlows().get(0).getDay().toString());
    	txtEndDate.setText(r.getFlows().get(r.getFlows().size()-1).getDay().toString());
    	txtNumMeasurements.setText(""+r.getFlows().size());
    	txtAvgFlow.setText(""+r.getFlowAvg());
    }

    @FXML
    void doSimulation(ActionEvent event) {

    	txtResult.clear();
    	
    	River r = this.boxRiver.getValue();
    	if(r==null)
    	{
    		txtResult.setText("Please select a river!\n");
    		return;
    	}
    	
    	String kText = txtK.getText();
    	Double k = null;
    	try {
    		k = Double.parseDouble(kText);
    	} catch(NumberFormatException e) {
    		txtResult.setText("Error! Insert a numeric value for the coefficient k!!\n");
    		return;
    	}
    	
    	if(k<=0) {
    		txtResult.setText("Error! Insert a positive value bigger than 0 for the coefficient k!!\n");
    		return;
    	}
    	
    	double maxCapacity = k*r.getFlowAvg()*86400*30;
    	double minFlow = 0.8*r.getFlowAvg()*86400;
    	
    	model.doSimulation(r, maxCapacity, minFlow);
    	txtResult.setText("The simulation on the "+r.getName()+"was successfully runned.\n"+model.getSimulationResult());
    	
    }

    @FXML
    void initialize() {
        assert boxRiver != null : "fx:id=\"boxRiver\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtStartDate != null : "fx:id=\"txtStartDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtEndDate != null : "fx:id=\"txtEndDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNumMeasurements != null : "fx:id=\"txtNumMeasurements\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAvgFlow != null : "fx:id=\"txtAvgFlow\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    
    public void setModel(Model model) {
    	this.model = model;
    	rivers.addAll(model.getRivers());
    	this.boxRiver.setItems(rivers);
    }
}
