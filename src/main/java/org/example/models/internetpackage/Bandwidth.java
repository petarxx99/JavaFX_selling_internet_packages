package org.example.models.internetpackage;

public enum Bandwidth {
    FLAT, GB1, GB2, GB5, GB10, GB100;

    public String numberOfGygabytes(){
        if(this.equals(FLAT)) return FLAT.toString();
        return this.toString().substring("GB".length());
    }

    public String getBandwidthWithMeasurementUnit(){
        if(this.equals(FLAT)) return FLAT.toString();
        return this.toString().substring("GB".length()) + " GB";
    }
}
