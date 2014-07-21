package lee.andrew.cerner.facial_recognition_search;

import java.util.ArrayList;
import java.util.List;

public class SearchResults {
    public static SearchResults searchResults = new SearchResults();
    private List<SearchResult> results = new ArrayList<SearchResult>();
    
    public void addResult(SearchResult result){
        results.add(result);
    }
    
    public List<SearchResult> getResults(){
        return results;
    }
    
    public SearchResult getResultFromName(String name){
        for(SearchResult result : results){
            if(result.getName().equals(name))
                return result;
        }
        return null;
    }
}
