package lee.andrew.cerner.facial_recognition_search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment_Main_Screen extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_pager, parent, false);
        
        return v; 
    }
}
