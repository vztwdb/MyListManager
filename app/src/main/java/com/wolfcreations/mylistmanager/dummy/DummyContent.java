package com.wolfcreations.mylistmanager.dummy;

import com.wolfcreations.mylistmanager.MyListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static  List<MyListItem> ITEMS = new ArrayList<MyListItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<Integer, MyListItem> ITEM_MAP = new HashMap<Integer, MyListItem>();

    private  final int COUNT = 25;

    public  String Name;

    public DummyContent( String name) {
        this.Name = name;
        ITEMS = new ArrayList<MyListItem>();
        ITEM_MAP = new HashMap<Integer, MyListItem>();
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createListItem(i, name));
        }
    }

    private  void addItem(MyListItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

    private MyListItem createListItem(int position, String name) {
        MyListItem aMyListItem =  new MyListItem(position, name + position, makeDetails(position, name));
        return aMyListItem;
    }

    private  String makeDetails(int position, String name) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about ").append(Name).append(": ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

}
