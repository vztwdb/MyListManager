package com.wolfcreations.mylistmanager.model;

import com.wolfcreations.mylistmanager.R;

/**
 * Created by Gebruiker on 17/01/2016.
 */
public enum TagEnum {
    BLACK(R.color.black,"Black"), RED(R.color.red, "Red"),
    GREEN(R.color.green, "Green"), BLUE(R.color.blue, "Blue"),YELLOW(R.color.yellow,"Yellow");
    private int code;
    private String name;
    private TagEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
    public int getTagColor() {
        return this.code;
    }
}
