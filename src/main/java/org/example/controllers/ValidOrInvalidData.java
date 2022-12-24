package org.example.controllers;

public class ValidOrInvalidData {
    private boolean isValid;
    private String whyDataIsntValid = null;
    
    public ValidOrInvalidData(boolean isValid, String whyDataIsntValid){
        this.isValid = isValid;
        if(!isValid){
            this.whyDataIsntValid = whyDataIsntValid;
        }
    }
    
    public boolean isValid(){return isValid;}
    public String getMessageWhyDataIsInvalid(){return whyDataIsntValid;}
}
