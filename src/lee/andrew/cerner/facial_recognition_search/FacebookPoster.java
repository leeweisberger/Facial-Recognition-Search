package lee.andrew.cerner.facial_recognition_search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

public class FacebookPoster {

    public void requestUserInfo(final Session session, final FragmentActivity fa, final File file){
      CameraPreview.makeDialogue(fa, "Scanning image", "Wait while we scan your image...");

        new Request(
                session,
                "/me",
                null,
                HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(Response response) {
                        GraphObject g = response.getGraphObject();
                        Log.d("Lee", g.toString());
                        onUserURLFound(session,fa,file,(String) g.getProperty("link"));
                    }
                }
                ).executeAsync();
    }

    private void requestPerson(final Session session, File file, final FragmentActivity fa, final String userURL) throws FileNotFoundException, InterruptedException {         
        Request request = Request.newUploadPhotoRequest(session,file, new Request.Callback() {

            @Override
            public void onCompleted(Response response) {       
                Response r = response;    
                GraphObject g = response.getGraphObject();
                String id = (String) g.getProperty("id");
                String picURL = "https://www.facebook.com/photo.php?fbid=" + id + "&set=p." + id + "&type=1";
                Log.d("Lee", picURL);


                SearchResult result=null;
                try {
                    result = new ServerRequester().execute(picURL, userURL,
                            LoginFragment.facebookUsername, LoginFragment.facebookPassword).get();

                } catch (InterruptedException
                        | ExecutionException e) {
                    result=null;
                    Log.d("Lee", "return nullposter");
                    e.printStackTrace();

                }
                CameraPreview.deleteDialogue();

                //                String name="Lee Weisberger";
                //                SearchResult result = new SearchResult(name);
                if(result!=null)
                    SearchResults.searchResults.addResult(result);
                ResultsFragment.adapter.notifyDataSetChanged();
//                Log.d("Lee", "name" + result.getName()+"name");
                setAlert(result,fa);
                

                //                                CameraFragment.button.setVisibility(View.VISIBLE);
                //                                CameraFragment.identifying.setVisibility(View.GONE);

                //                            }
                //                        }).executeAsync();
            }
        });
        Log.d("Lee", "request made");
        request.executeAsync();
    }

    private void setAlert(final SearchResult result, final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        if(result==null)
            builder.setTitle("Sorry, the subject could not be identified");
        else{
            builder.setTitle("Success! Identified as " + result.getName());
            builder.setPositiveButton("View Info", new DialogInterface.OnClickListener() { 
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(context,ResultViewerActivity.class);
                    i.putExtra("result", result.getName());
                    context.startActivity(i);
                }
            });
        }
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    
    private void onUserURLFound(Session session, FragmentActivity fa, File file, String userURL){
        try {
            requestPerson(session, file, fa,userURL);
        } catch (FileNotFoundException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
