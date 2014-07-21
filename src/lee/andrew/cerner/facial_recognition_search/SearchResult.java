package lee.andrew.cerner.facial_recognition_search;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SearchResult {
    private TreeMap<String,String> items;
    public SearchResult(){
        items = new TreeMap<String,String>(Collections.reverseOrder());
    }
    
    public String getName(){
        return items.get("name");
    }
    
    public void addItem(String key, String value){
        items.put(key, value);
    }
    
    public TreeMap<String,String> getMap(){
        return items;
    }
}
