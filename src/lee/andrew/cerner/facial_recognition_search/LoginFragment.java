package lee.andrew.cerner.facial_recognition_search;

import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, 
            ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.login, container, false);
        LoginButton authButton = (LoginButton) view.findViewById(R.id.facebookAuthButton);
        authButton.setFragment(this);
        authButton.setPublishPermissions(Arrays.asList("publish_actions"));
        Button b = (Button) view.findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                makeMeRequest(Session.getActiveSession());               
            }
        });
        return view;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }
    
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
    
    private void makeMeRequest(final Session session) {
        
         Request request = Request.newStatusUpdateRequest(session,"Hello World", 
                new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                if (session == Session.getActiveSession()) {
                    Response r = response;
                }
                if (response.getError() != null) {
                }
            }

           
        });
        request.executeAsync();
    } 
    
    
}
