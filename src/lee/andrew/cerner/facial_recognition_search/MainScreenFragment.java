package lee.andrew.cerner.facial_recognition_search;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainScreenFragment extends Fragment {
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
