package lee.andrew.cerner.facial_recognition_search;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ResultsFragment extends ListFragment {
    public static ResultsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        adapter = new ResultsAdapter(getActivity());
        setListAdapter(adapter);
        
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText("No Results");     
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Log.d("Lee", "resumed");
        if(SearchResults.searchResults.getResults().size()>this.getListAdapter().getCount())
            adapter.notifyDataSetChanged();
    }
    
}
