package com.gregghz.SyncReader.data;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class SRBookStore {
	public static List<SRBook> ITEMS = new Vector<SRBook>();
    public static Map<String, SRBook> ITEM_MAP = new ConcurrentHashMap<String, SRBook>();

    public static void addItem(SRBook item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.path(), item);
    }
}
