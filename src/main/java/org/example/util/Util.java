package org.example.util;

import javafx.collections.ObservableList;

import java.util.List;

public class Util {
    public static boolean stringIsntNullNorBlank(String string){
        if(string == null) return false;
        return !string.isBlank();
    }

    public static boolean listIsNullOrHasNoElements(List list){
        if(list == null) return true;
        return list.size() == 0;
    }

}
