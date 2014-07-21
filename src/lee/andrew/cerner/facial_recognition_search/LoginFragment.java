package lee.andrew.cerner.facial_recognition_search;

import java.util.Arrays;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

public class LoginFragment extends Fragment {
    public static String facebookPassword;
    public static String facebookUsername;
    private RelativeLayout layout;
    private ImageView checkon1;
    private ImageView checkon2;
    private ImageView checkoff1;
    private ImageView checkoff2;
    private ImageView checkoff3;
    private ImageView checkon3;

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
        layout = (RelativeLayout) view.findViewById(R.id.continue_layout);
        checkoff1 = (ImageView) view.findViewById(R.id.checkoff1);
        checkoff2 = (ImageView) view.findViewById(R.id.checkoff2);
        checkon1 = (ImageView) view.findViewById(R.id.checkon1);
        checkon2 = (ImageView) view.findViewById(R.id.checkon2);
        checkon3 = (ImageView) view.findViewById(R.id.checkon3);
        checkoff3 = (ImageView) view.findViewById(R.id.checkoff3);
        
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        facebookUsername= sharedPref.getString("facebookUsername", "");
        facebookPassword=sharedPref.getString("facebookPassword", "");
        
        if(isLoggedIn()){
            Log.d("Lee", "logged in");
            toggleChecks(checkoff1, checkon1);
        }
        if(facebookUsername!=null && !facebookUsername.isEmpty())
            toggleChecks(checkoff2,checkon2);

        if(facebookPassword!=null && !facebookPassword.isEmpty())
            toggleChecks(checkoff3,checkon3);
        Log.d("Lee", checkoff1.getVisibility()+"");
        Log.d("Lee",checkon2.getVisibility()+"");
        isChecksOn();
        
        




        LoginButton facebookAuthButton = (LoginButton) view.findViewById(R.id.facebookAuthButton);
        facebookAuthButton.setFragment(this);
        facebookAuthButton.setPublishPermissions(Arrays.asList("publish_actions","user_friends"));

        Button facialAuthButton  = (Button) view.findViewById(R.id.facial_auth_username);
        facialAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlert1("Enter Facebook Username");
                isChecksOn();
            }
        });

        Button facialAuthButtonPassword  = (Button) view.findViewById(R.id.facial_auth_password);
        facialAuthButtonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlert2("Enter Facebook Password");
                isChecksOn();
            }
        });


        Button continueButton = (Button) view.findViewById(R.id.continueButton);

        continueButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),MainScreenActivity.class);
                saveCredentials();
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
            toggleChecks(checkoff1, checkon1);

        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
            toggleChecks(checkon1, checkoff1);
        }
        isChecksOn();
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



    private void setAlert1(String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);

        // Set up the input
        final EditText input = new EditText(getActivity());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        input.setText(facebookUsername);


        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toggleChecks(checkoff2, checkon2);
                facebookUsername= input.getText().toString();
                isChecksOn();
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

    private void setAlert2(String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);

        // Set up the input
        final EditText input = new EditText(getActivity());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        input.setText(facebookPassword);


        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toggleChecks(checkoff3, checkon3);
                facebookPassword = input.getText().toString();
                isChecksOn();
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

    public void toggleChecks(ImageView checkoff,ImageView checkon){
        checkoff.setVisibility(View.GONE);
        checkon.setVisibility(View.VISIBLE);
    }
    private void saveCredentials() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("facebookUsername", facebookUsername);
        editor.putString("facebookPassword", facebookPassword);
        editor.commit();
    }
    
    private void isChecksOn(){
        if(checkon1.getVisibility()==View.VISIBLE && checkon2.getVisibility()==View.VISIBLE && checkon3.getVisibility()==View.VISIBLE)
            layout.setVisibility(View.VISIBLE);
        else{
            layout.setVisibility(View.GONE);
        }
    }


}
