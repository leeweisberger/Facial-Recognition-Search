package lee.andrew.cerner.facial_recognition_search.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;




public class FacebookServerMain extends Thread{

    private Socket clientSocket;
    private OutputStream os;
    private DataOutputStream dos;
    private InputStream is;
    private DataInputStream dis;

    static ArrayList<FacebookServerMain> threadPool = new ArrayList<FacebookServerMain>();
    static ArrayList<Socket> sockets = new ArrayList<Socket>();

    private static final int listenPort = 12345;
    private int threadIndex;
    private int socketIndex;

    private String[] params = new String[4];

    public FacebookServerMain(Socket clientSocket, int threadIndex, int socketIndex) throws ClassNotFoundException {
        this.threadIndex = threadIndex;
        this.socketIndex = socketIndex;
        try {
            this.clientSocket = clientSocket;
            os = clientSocket.getOutputStream();
            dos = new DataOutputStream(os);
            is = clientSocket.getInputStream();
            dis = new DataInputStream(is);
        } catch (IOException e) {
            System.err.println("Failed to construct new thread");
            e.printStackTrace();
        } 

    }

    public void run() {
        try {
            for(int i=0; i<params.length; i++) {
                params[i] = dis.readUTF();
            }
            HashMap<String, String> result = FacebookSuggestionParser.getInfo(params[0], params[1], params[2], params[3]);
            System.out.println("Got result");
            if(result != null) {
                Iterator it = result.entrySet().iterator();
                while(it.hasNext()) {
                    Map.Entry<String, String> temp = (Entry<String, String>) it.next();
                    dos.writeUTF(temp.getKey());
                    dos.writeUTF(temp.getValue());
                }
            } else {
                dos.writeUTF("null");
                System.out.println("Sent null");
            }
            dos.writeUTF("done");
            endConnection();
        } catch(IOException e) {
            System.err.println("Error reading stream");
            e.printStackTrace();
        } catch(Exception e) {
            System.err.println("Failed to get Facebook suggestion");
            e.printStackTrace();
        }
    }

    private void endConnection() {
        try {
            dos.close();
            os.close();
            dis.close();
            is.close();

            clientSocket.close();
            synchronized(threadPool) {
                sockets.remove(socketIndex);
                threadPool.remove(threadIndex);
            }
        } catch (IOException e) {
            System.err.println("Failed to close streams");
            e.printStackTrace();
        } 
    }



    public static void main(String args[]) throws ClassNotFoundException {
        try {
            @SuppressWarnings("resource")
            ServerSocket serverSocket = new ServerSocket(listenPort);
            while(true) {
                Socket temp = serverSocket.accept();
                synchronized(threadPool) {
                    sockets.add(temp);
                    FacebookServerMain tempRef = new FacebookServerMain(temp, threadPool.size(), sockets.size()-1);
                    threadPool.add(tempRef);
                    tempRef.start();
                }
            }
        } catch (IOException e) {
            System.err.println("Server failed");
            e.printStackTrace();
        }

    }


}

//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;


//public class Test {
//
//    public static void main(String[] args) throws Exception {
//        HashMap<String, String> result = FacebookSuggestionParser.getInfo("https://www.facebook.com/photo.php?fbid=10202935025000857&l=b5ef04b471", "https://www.facebook.com/app_scoped_user_id/10204331492311667/", "ahayes2425@gmail.com", "andrew2425");
//        if(result != null) {
//            Iterator it = result.entrySet().iterator();
//            while(it.hasNext()) {
//                Map.Entry<String, String> temp = (Entry<String, String>) it.next();
//                System.out.println(temp.getKey() + ": " + temp.getValue());
//            }
//        } else {
//            System.out.println("No suggested tag!");
//        }
//        /*
//        Document test = (Document) Jsoup.connect("https://www.facebook.com/lee.weisberger").get();
//        Elements test2 = test.getElementsByClass("_70k");
//        System.out.println(test.toString());
//        System.out.println("test2: " + test2);
//        */
//    }

//}