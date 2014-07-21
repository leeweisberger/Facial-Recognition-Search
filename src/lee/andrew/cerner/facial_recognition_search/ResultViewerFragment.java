package lee.andrew.cerner.facial_recognition_search;

import java.util.Set;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

public class ResultViewerFragment extends ListFragment {
//    public static ResultViewAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        SearchResult result = SearchResults.searchResults.getResultFromName(getActivity().getIntent().getStringExtra("result"));
        
        
        Set<String> resultKeysSet = result.getMap().keySet();
        String[] resultKeys = resultKeysSet.toArray(new String[resultKeysSet.size()]);
        ResultViewAdapter adapter = new ResultViewAdapter(getActivity(), resultKeys,result);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.d("Lee", result.getName());
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            if (NavUtils.getParentActivityName(getActivity()) != null) 
                NavUtils.navigateUpFromSameTask(getActivity());
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }
}
