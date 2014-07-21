package lee.andrew.cerner.facial_recognition_search;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ResultViewAdapter extends ArrayAdapter<String> {
    private final Context context;
    private SearchResult result;
    public ResultViewAdapter(Context context, String[] resultKeys,SearchResult result) {    
        super(context, R.layout.result_viewer,resultKeys);
        this.context = context;
        this.result=result;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        if(view==null)
            view = inflater.inflate(R.layout.result_viewer, parent, false);
        Log.d("Lee", "adapter");
        TextView key = (TextView) view.findViewById(R.id.key);
        TextView value = (TextView) view.findViewById(R.id.value);
        key.setText((String) result.getMap().keySet().toArray()[position]);
        Log.d("Lee", (String) key.getText());
        value.setText(result.getMap().get((String)key.getText()));
        return view;
    }
}
