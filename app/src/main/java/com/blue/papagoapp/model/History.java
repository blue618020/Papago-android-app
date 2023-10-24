package com.blue.papagoapp.model;

import java.io.Serializable;

public class History implements Serializable {

    public String text;
    public String target;
    public String translatedText;

    public History(String text, String target, String translatedText) {
        this.text = text;
        this.target = target;
        this.translatedText = translatedText;
    }
}
