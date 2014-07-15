package lee.andrew.cerner.facial_recognition_search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PageFragment extends Fragment {
    int mCurrentPage;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        mCurrentPage = data.getInt("current_page", 0); 
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_page, container,false);
        TextView tv = (TextView ) v.findViewById(R.id.tv);
        tv.setText("You are viewing the page #" + mCurrentPage + "\n\n" + "Swipe Horizontally left / right");
        return v;
    }
}
