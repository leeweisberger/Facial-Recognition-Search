package lee.andrew.cerner.facial_recognition_search;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ResultsAdapter extends ArrayAdapter<SearchResult> {
    private final Context context;

    public ResultsAdapter(Context context) {
        
        super(context, R.layout.results_list, SearchResults.searchResults.getResults());
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("Lee", "adapters");

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        if(view==null)
            view = inflater.inflate(R.layout.results_list, parent, false);
        
        TextView person = (TextView) view.findViewById(R.id.person);
        Button viewResults = (Button) view.findViewById(R.id.view_results);
        viewResults.setOnClickListener(new ResultListener(SearchResults.searchResults.getResults().get(position).getName()));
               
        person.setText(SearchResults.searchResults.getResults().get(position).getName());
        return view;
    }
    
    class ResultListener implements View.OnClickListener{
        private String resultName;
        public ResultListener(String resultName){
            this.resultName=resultName;
        }
        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, ResultViewerActivity.class);
            i.putExtra("result", resultName);          
            context.startActivity(i);
        }     
    }
    
    
}
