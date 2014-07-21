package lee.andrew.cerner.facial_recognition_search;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;

import android.os.AsyncTask;
import android.util.Log;

public class ServerRequester extends AsyncTask<String, Void, SearchResult> {
    private static final String URL = "10.184.187.169";
    private static final int port = 12345;
    private Socket socket;
    private OutputStream os;
    private DataOutputStream dos;
    private DataInputStream dis;


    public ServerRequester() {

    }

    public SearchResult getName(String picURL,String userURL,String user,String pass) {
        SearchResult items = new SearchResult();
        
        try {
            Log.d("Lee", "getName");
            socket = new Socket(URL, port);
            Log.d("Lee", "socket.toString()");
            os = socket.getOutputStream();
            dos = new DataOutputStream(os);

            InputStream is = socket.getInputStream();
            dis = new DataInputStream(is);

        } catch (UnknownHostException e) {
            Log.d("errorr", "return null");
            return null;
        } catch (IOException e) {
            Log.d("errorr", "return null");
            return null;
        }

        try {
            dos.writeUTF(picURL);
            dos.writeUTF(userURL);
            dos.writeUTF(user);
            dos.writeUTF(pass);
            
            String item;
            while(!(item=dis.readUTF()).equals("done")){
                String key = item;
                if(key.equals("null"))
                    return null;
                
                String value = dis.readUTF();
                if(key.equals("Email"))
                items.addItem(key, value.split(" ")[0]);
            }
            return items;


        } catch (IOException ioe) {
            Log.d("errorr", "return null");
            return null;

        }
    }

    @Override
    protected SearchResult doInBackground(String... params) {

        String picURL = params[0];
        String userURL = params[1];
        String user = params[2];
        String pass = params[3];
       
        try {
            SearchResult items = getName(picURL,userURL,user,pass);

            return items;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("errorr", "return null");
            return null;
        }
    }

}
