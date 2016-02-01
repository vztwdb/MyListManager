package com.wolfcreations.mylistmanager.model;

import com.wolfcreations.mylistmanager.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gebruiker on 17/01/2016.
 */
public enum TagEnum {
    BLACK(R.color.black,"Black"), RED(R.color.red, "Red"),
    GREEN(R.color.green, "Green"), BLUE(R.color.blue, "Blue"),YELLOW(R.color.yellow,"Yellow");
    private int code;
    private String name;

    TagEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getTagColor() {
        return this.code;
    }

    public String getTagName() {
        return this.name;
    }

    // Reverse-lookup map for getting a day from an abbreviation
    private static final Map<String, TagEnum> lookup = new HashMap<String, TagEnum>();

    static {
        for (TagEnum d : TagEnum.values()) {
            lookup.put(d.getAbbreviation(), d);
        }
    }

    public String getAbbreviation() {
        return name;
    }

    public static TagEnum get(String name) {
        return lookup.get(name);
    }
}
