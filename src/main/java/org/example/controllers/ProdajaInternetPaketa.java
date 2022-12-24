package org.example.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.models.internetpackage.Bandwidth;
import org.example.models.internetpackage.InternetPackage;
import org.example.util.Util;

import java.net.URL;
import java.util.*;

public class ProdajaInternetPaketa {

    InternetPackage internetPackage;
    ObservableList<InternetPackage> internetPackages = FXCollections.<InternetPackage>observableArrayList();

    @FXML
    private Parent root;
    @FXML
    private TextField firstName, lastName, address;

    @FXML
    private ToggleGroup internetSpeed, bandwidth, contractDuration;

    @FXML
    private ToggleButton MBPS2, MBPS5, MBPS10, MBPS20, MBPS50, MBPS100,
    GB1, GB2, GB5, GB10, GB100, FLAT,
    durationInMonths12, durationInMonths24;

    @FXML
    private Button saveButton, clearButton, deleteTableRowButton;

    @FXML
    private TextField idToDelete;

    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    public ProdajaInternetPaketa() {
    }

    @FXML
    private void initialize() {
        internetPackage = new InternetPackage();
        internetPackage.reset();

        internetPackage.firstNameProperty().bindBidirectional(firstName.textProperty());
        internetPackage.lastNameProperty().bindBidirectional(lastName.textProperty());
        internetPackage.addressProperty().bindBidirectional(address.textProperty());

        internetSpeed.selectedToggleProperty().addListener((obsValue, oldValue, newValue)->
                internetSpeedChanged(newValue));

        bandwidth.selectedToggleProperty().addListener((obsValue, oldValue, newValue)->{
            bandwidthChanged(newValue);
        });

        contractDuration.selectedToggleProperty().addListener((obsValue, oldValue, newValue)->
                contractDurationChanged(newValue));

        System.out.println("Initializing ProdajaInternetPaketa...");
        System.out.println("Location = " + location);
        System.out.println("Resources = " + resources);
    }

    private void internetSpeedChanged(Toggle toggle){
        if(toggle == null) {
            internetPackage.resetInternetSpeedInMegabytesPerSecond();
        } else {
            ToggleButton clickedButton = (ToggleButton) toggle;
            final double NEW_INTERNET_SPEED_IN_MBPS = toMegabytesPerSecond(clickedButton);
            internetPackage.setInternetSpeedInMegabytesPerSecond(NEW_INTERNET_SPEED_IN_MBPS);
        }
    }

    private void bandwidthChanged(Toggle toggle){
        if(toggle == null){
            internetPackage.resetBandwidth();
        } else {
            ToggleButton clickedButton = (ToggleButton) toggle;
            internetPackage.setBandwidth(Bandwidth.valueOf(clickedButton.getId()));
        }
    }

    private void contractDurationChanged(Toggle toggle){
        if(toggle == null){
            internetPackage.resetContractDurationInMonths();
        } else {
            ToggleButton clickedButton = (ToggleButton) toggle;
            final int NEW_CONTRACT_DURATION_IN_MONTHS = toContractDurationInMonths(clickedButton);
            internetPackage.setContractDurationInMonths(NEW_CONTRACT_DURATION_IN_MONTHS);
        }
    }


    @FXML
    private void deletePackage() {
        try{
            long id = Long.parseLong(idToDelete.getText());
            boolean deletedSuccessfully = internetPackage.deleteSavedPackage(id);
            showMessageBasedOnACondition(deletedSuccessfully,
                    "Package has been deleted. Deleted id: " + id,
                    "There was a problem while deleting the package with id: " + id);
        } catch(NumberFormatException exception){
            showErrorMessage("You didn't enter valid id to delete.");
        }
    }

    @FXML
    private void seeSavedPackages(){
        root.setVisible(false);
        ObservableList<InternetPackage> savedPackages = internetPackage.getSavedPackages();

        if(Util.listIsNullOrHasNoElements(savedPackages)){
            showErrorMessage("There are no saved packages.");
        } else {
            showSavedPackages(savedPackages);
        }
        root.setVisible(true);
    }

    @FXML
    private void savePackage(){
        root.setVisible(false);

        ValidOrInvalidData data = internetPackage.isValid();
        if(data.isntValid()){
            showErrorMessage(data.getMessageWhyDataIsInvalid());
        } else {
            boolean savingSuccessful = internetPackage.save();
            showMessageBasedOnACondition(savingSuccessful,
                    "Internet package has been successfully saved.",
                    "There was a problem while saving the package.");
        }

        root.setVisible(true);
    }

    @FXML
    private void clearForm(){
        internetPackage.reset();

        internetSpeed.selectToggle(null);
        bandwidth.selectToggle(null);
        contractDuration.selectToggle(null);
    }


    @FXML
    private void queryPackages(){
        root.setVisible(false);
        ObservableList<InternetPackage> savedPackages = internetPackage.getInternetPackagesLikeThisOne();

        if(Util.listIsNullOrHasNoElements(savedPackages)){
            showErrorMessage("There are no saved packages that correspond to the data that you have inputed into this window.");
        } else {
            showSavedPackages(savedPackages);
        }
        root.setVisible(true);

    }

    private double toMegabytesPerSecond(ToggleButton clickedButton){
        final String BUTTON_ID = clickedButton.getId();
        final String SPEED_IN_MBPS_AS_STRING = BUTTON_ID.substring("MBPS".length());
        return Double.parseDouble(SPEED_IN_MBPS_AS_STRING);
    }

    private int toContractDurationInMonths(ToggleButton clickedButton){
        final String BUTTON_ID = clickedButton.getId();
        final String DURATION_IN_MONTHS_AS_STRING = BUTTON_ID.substring("durationInMonths".length());
        return Integer.parseInt(DURATION_IN_MONTHS_AS_STRING);
    }

    private void showSavedPackages(ObservableList<InternetPackage> soldPackages){
        VBox root = new VBox();

        GridPane gridPane = new GridPane();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridPane);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);

        gridPane.setStyle("-fx-border-width: 3px; -fx-border-color: orange;");

        makeARowFromObjects(gridPane, 0, "id", "first name", "last name", "address", "internet speed", "bandwidth",
                "contract duration", "contract expiration");

        for(int i=0; i<soldPackages.size(); i++){
            makeARowFromInternetPackage(soldPackages.get(i), gridPane, i+1);
        }

        root.getChildren().add(scrollPane);
        final double SCREEN_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight() * 9/10.0;
        Scene scene = new Scene(root, SCREEN_HEIGHT, SCREEN_HEIGHT);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    private void makeARowFromInternetPackage(InternetPackage internetPackage, GridPane gridPane, int row){
        makeARowFromObjects(gridPane, row,
                internetPackage.getId(),
                internetPackage.getFirstName(),
                internetPackage.getLastName(),
                internetPackage.getAddress(),
                internetPackage.getInternetSpeedInMegabytesPerSecond() + "MBPS",
                internetPackage.getBandwidth().getBandwidthWithMeasurementUnit(),
                internetPackage.getContractDurationInMonths() / 12.0 + " years",
                internetPackage.timeLeftBeforeContractExpiresToString());
    }

    private void makeARowFromObjects(GridPane gridPane, int row, Object... objects){
        for(int i=0; i<objects.length; i++){
            TextArea textArea = new TextArea(objects[i].toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            gridPane.add(textArea, i, row);
        }
    }

    private void showErrorMessage(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(errorMessage);
        alert.show();
    }

    private void showMessage(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

    private void showConfirmMessage(String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        alert.show();
    }
    private void showMessageBasedOnACondition(boolean condition, String messageIfTrue, String messageIfFalse){
        if(condition){
            showConfirmMessage(messageIfTrue);
        } else {
            showErrorMessage(messageIfFalse);
        }
    }

}
