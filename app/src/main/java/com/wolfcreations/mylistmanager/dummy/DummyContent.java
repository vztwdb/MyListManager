package com.wolfcreations.mylistmanager.dummy;

import com.wolfcreations.mylistmanager.model.MyListItem;
import com.wolfcreations.mylistmanager.model.TagEnum;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    public static Map<String, MyListItem> ITEM_MAP = new HashMap<String, MyListItem>();

    private  final int COUNT = 8;

    public  static String Name = "Test";

    public DummyContent( String name) {
        this.Name = name;
        ITEMS = new ArrayList<MyListItem>();
        ITEM_MAP = new HashMap<String, MyListItem>();
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createListItem(i, name));
        }
    }

    private  void addItem(MyListItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId().toString(), item);
    }

    private MyListItem createListItem(int position, String name) {
        MyListItem aMyListItem =  new MyListItem(position, name + position, makeDetails(position, name));
        Random  rnd;
        Date    dt;
        long    ms;
// Get a new random instance, seeded from the clock
        rnd = new Random();
// Get an Epoch value roughly between 1940 and 2010
// -946771200000L = January 1, 1940
// Add up to 70 years to it (using modulus on the next long)
        ms = -946771200000L + (Math.abs(rnd.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
// Construct a date
        dt = new Date(ms);
        aMyListItem.duedate = dt;
        Random ran = new Random();
        int x = ran.nextInt(5) ;
        switch (x) {
            case 0:  aMyListItem.myTag = TagEnum.BLACK;
                break;
            case 2:  aMyListItem.myTag = TagEnum.BLUE;
                break;
            case 3:  aMyListItem.myTag = TagEnum.GREEN;
                break;
            case 4:  aMyListItem.myTag = TagEnum.RED;
                break;
            case 5: aMyListItem.myTag = TagEnum.YELLOW;
                break;
            default: aMyListItem.myTag = TagEnum.GREEN;
                break;
        }

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
