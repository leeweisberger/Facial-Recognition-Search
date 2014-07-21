package lee.andrew.cerner.facial_recognition_search;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class ResultViewerActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ResultViewerFragment();
    }

}
