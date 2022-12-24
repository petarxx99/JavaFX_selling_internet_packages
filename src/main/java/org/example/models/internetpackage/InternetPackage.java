package org.example.models.internetpackage;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.controllers.ValidOrInvalidData;
import org.example.util.Util;

import java.sql.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class InternetPackage{
    public InternetPackage(){}

    private static final double INVALID_INTERNET_SPEED = -1;
    private static final int INVALID_CONTRACT_DURATION = -1;

    private String databaseAddress = "localhost",
            databaseName = "db_javafx_assignment",
            username="assignment_user",
            password="password",
            tableName = "internet_packages";
    private int databasePort = 3306;
    private final String stringForMySQLConnection = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s", databaseAddress, databasePort, databaseName, username, password);
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();
    private final DoubleProperty internetSpeedInMegabytesPerSecond = new SimpleDoubleProperty(INVALID_INTERNET_SPEED);
    private final ObjectProperty<Bandwidth> bandwidth = new SimpleObjectProperty<>(null);
    private final IntegerProperty contractDurationInMonths = new SimpleIntegerProperty(INVALID_CONTRACT_DURATION);
    private Date contractSignignDate;

    private long id;
    public long getId(){return id;}
    public LocalDate getContractExpirationDate(){
        return contractSignignDate.toLocalDate().plusMonths(getContractDurationInMonths());
    }

    public int getContractDurationInMonths() {
        return contractDurationInMonths.get();
    }

    public IntegerProperty contractDurationInMonthsProperty() {
        return contractDurationInMonths;
    }

    public void setContractDurationInMonths(int contractDurationInMonths) {
        this.contractDurationInMonths.set(contractDurationInMonths);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public double getInternetSpeedInMegabytesPerSecond() {
        return internetSpeedInMegabytesPerSecond.get();
    }

    public DoubleProperty internetSpeedInMegabytesPerSecondProperty() {
        return internetSpeedInMegabytesPerSecond;
    }

    public void setInternetSpeedInMegabytesPerSecond(double internetSpeedInMegabytesPerSecond) {
        this.internetSpeedInMegabytesPerSecond.set(internetSpeedInMegabytesPerSecond);
    }

    public Bandwidth getBandwidth() {
        return bandwidth.get();
    }

    public ObjectProperty<Bandwidth> bandwidthProperty() {
        return bandwidth;
    }

    public void setBandwidth(Bandwidth bandwidth) {
        this.bandwidth.set(bandwidth);
    }

    public void resetInternetSpeedInMegabytesPerSecond(){
        setInternetSpeedInMegabytesPerSecond(INVALID_INTERNET_SPEED);
    }
    public void resetContractDurationInMonths(){
        setContractDurationInMonths(INVALID_CONTRACT_DURATION);
    }


    @Override
    public String toString(){
        List<String> data = getData();
        StringBuilder sb = new StringBuilder();
        data.forEach(string -> sb.append(string + "\n"));
        return sb.toString();
    }

    public ArrayList<String> getData(){
        ArrayList<String> data = new ArrayList<>();
        data.add("id: " + id);
        data.add("first name: " + getFirstName());
        data.add("last name: " + getLastName());
        data.add("address: " + getAddress());
        data.add("internet speed in MBPS: " + getInternetSpeedInMegabytesPerSecond());
        data.add("bandwidth: " + getBandwidth().numberOfGygabytes() + "GB");
        data.add("contract duration: " + getContractDurationInMonths() / 12.0 + " years");
        if(contractSignignDate != null){
            Period howMuchBeforeContractExpires = getHowMuchTimeBeforeContractExpires();
            data.add(String.format("contract expires in %d years, %d months and %d days", howMuchBeforeContractExpires.getYears(), howMuchBeforeContractExpires.getMonths(), howMuchBeforeContractExpires.getDays()));
        }
        return data;
    }

    public Period getHowMuchTimeBeforeContractExpires(){
        LocalDate contractSignignDateLD = contractSignignDate.toLocalDate();

        return Period.between(
                LocalDate.now(),
                contractSignignDateLD.plusMonths(getContractDurationInMonths()));
    }

    public String timeLeftBeforeContractExpiresToString(){
        Period timeLeft = getHowMuchTimeBeforeContractExpires();
        if(timeLeft.isNegative()){
            return "Contract has already expired.";
        }
        return String.format("Contract expires in %s, %s, %s.",
                timeLeft.getYears() +  " year" + getLetterSifQuantityIsMultiple(timeLeft.getYears()),
                timeLeft.getMonths() + " month" + getLetterSifQuantityIsMultiple(timeLeft.getMonths()),
                timeLeft.getDays() + " day" + getLetterSifQuantityIsMultiple(timeLeft.getDays()));
    }

    private String getLetterSifQuantityIsMultiple(int quantity){
        if(quantity == 1) return "";
        return "s";
    }
    private String addSforMultiple(int quantity, String word){
        if(quantity==1){
            return word;
        }
        return word + "s";
    }
    public void reset(){
        firstName.set("");
        lastName.set("");
        address.set("");
        resetInternetSpeedInMegabytesPerSecond();
        resetBandwidth();
        resetContractDurationInMonths();
    }

    public void resetBandwidth(){
        bandwidth.set(null);
    }
    public ValidOrInvalidData isValid(){
        if(!Util.stringIsntNullNorBlank(getFirstName())){
            return new ValidOrInvalidData(false, "First name is invalid.");
        }
        if(!Util.stringIsntNullNorBlank(getLastName())){
            return new ValidOrInvalidData(false, "Last name is invalid.");
        }
        if(!Util.stringIsntNullNorBlank(getAddress())){
            return new ValidOrInvalidData(false, "Address is invalid.");
        }
        if(getInternetSpeedInMegabytesPerSecond() == INVALID_INTERNET_SPEED){
            return new ValidOrInvalidData(false, "Error with internet speed.");
        }
        if(getBandwidth() == null){
            return new ValidOrInvalidData(false, "Error with bandwidth.");
        }
        if(getContractDurationInMonths() == INVALID_CONTRACT_DURATION){
            return new ValidOrInvalidData(false, "Error with contract duration.");
        }

        return new ValidOrInvalidData(true, null);
    }


    private String createSQLforSaving(){
        StringBuilder sql = new StringBuilder(String.format("INSERT INTO %s ", tableName));

        sql.append("(first_name, last_name, address, internet_speed_in_megabytes_per_second," +
                "bandwidth, contract_duration_in_months, contract_signign_date)");

        Date today = Date.valueOf(LocalDate.now());
        sql.append(String.format(" VALUES ('%s', '%s', '%s', %s, '%s', %s, '%s');",
                getFirstName(), getLastName(), getAddress(), getInternetSpeedInMegabytesPerSecond(),
                getBandwidth(), getContractDurationInMonths(), today));

        return sql.toString();
    }
    public boolean save(){
        final String SQL = createSQLforSaving();

        try(Connection connection = DriverManager.getConnection(stringForMySQLConnection)){
            Statement statement = connection.createStatement();
            final int ROWS_AFFECTED = statement.executeUpdate(SQL);
            return ROWS_AFFECTED == 1;
        } catch(SQLException exception){
            exception.printStackTrace();
            return false;
        }
    }


    public ObservableList<InternetPackage> getSavedPackages(){
        final String SQL = String.format("SELECT * FROM %s;", tableName);
        return getPackagesFromDatabase(SQL);
    }

    private ObservableList<InternetPackage> getPackagesFromDatabase(String sqlQuery){
        ObservableList<InternetPackage> savedPackages = FXCollections.<InternetPackage>observableArrayList();

        try(Connection connection = DriverManager.getConnection(stringForMySQLConnection)){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while(resultSet.next()){
                InternetPackage internetPackage = new InternetPackage();
                internetPackage.id = resultSet.getLong(1);
                internetPackage.setFirstName(resultSet.getString(2));
                internetPackage.setLastName(resultSet.getString(3));
                internetPackage.setAddress(resultSet.getString(4));
                internetPackage.setInternetSpeedInMegabytesPerSecond(resultSet.getDouble(5));
                internetPackage.setBandwidth(Bandwidth.valueOf(resultSet.getString(6)));
                internetPackage.setContractDurationInMonths(resultSet.getInt(7));
                internetPackage.contractSignignDate = resultSet.getDate(8);
                savedPackages.add(internetPackage);
            }
            return savedPackages;
        } catch(SQLException exception){
            exception.printStackTrace();
            return null;
        }
    }

    public boolean deleteSavedPackage(long id){
        final String SQL = String.format("DELETE FROM %s WHERE %s.id=%s;", tableName, tableName, id);

        try(Connection connection = DriverManager.getConnection(stringForMySQLConnection)){
            Statement statement = connection.createStatement();
            int ROWS_AFFECTED = statement.executeUpdate(SQL);
            return ROWS_AFFECTED == 1;
        } catch(SQLException exception){
            exception.printStackTrace();
            return false;
        }
    }


}
