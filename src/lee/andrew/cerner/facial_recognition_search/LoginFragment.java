package lee.andrew.cerner.facial_recognition_search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.widget.LoginButton;

public class LoginFragment extends Fragment {
    private String facebookPassword;
    private String login_input;
    private View continueButton;
    
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
        LoginButton facebookAuthButton = (LoginButton) view.findViewById(R.id.facebookAuthButton);
        facebookAuthButton.setFragment(this);
        facebookAuthButton.setPublishPermissions(Arrays.asList("publish_actions"));

        Button facialAuthButton  = (Button) view.findViewById(R.id.facialAuthButton);
        facialAuthButton.setOnClickListener(new View.OnClickListener() {

            

            @Override
            public void onClick(View v) {
                setAlert("Enter Facebook Password");
                if(facebookPassword!=null)
                    continueButton.setVisibility(View.VISIBLE);
            }
        });
        
        continueButton = (Button) view.findViewById(R.id.continueButton);
        continueButton.setVisibility(View.INVISIBLE);
        
        continueButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),MainScreenActivity.class);
                i.putExtra("facebookPassword", facebookPassword);
                startActivity(i);
                
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

    public static void getPictureURL(final Session session, File file) throws FileNotFoundException {      
        Request request = Request.newUploadPhotoRequest(session,file, 
                new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                if (session == Session.getActiveSession()) {
                    Response r = response;                    
                    GraphObject g = r.getGraphObject();
                    new Request(
                            session,
                            (String) g.getProperty("id"),
                            null,
                            HttpMethod.GET,
                            new Request.Callback() {
                                public void onCompleted(Response response) {
                                    Log.d("Lee", (String) response.getGraphObject().getProperty("link"));
                                }
                            }
                        ).executeAsync();
                    
                    
                    
                }
                if (response.getError() != null) {
                    Log.d("lee", "error");
                }
            }
        });
        request.executeAsync();
    } 

    private void setAlert(String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);

        // Set up the input
        final EditText input = new EditText(getActivity());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
            @Override
            public void onClick(DialogInterface dialog, int which) {
                facebookPassword = input.getText().toString();
                if(isLoggedIn())
                    continueButton.setVisibility(View.VISIBLE);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    
    public boolean isLoggedIn() {
        Session session = Session.getActiveSession();
        return (session != null && session.isOpened());
    }


}
