package lee.andrew.cerner.facial_recognition_search;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

public class FacebookSuggestionParser extends AsyncTask<String, Void, String> {
    HttpUrlConnectionExample http;
    public FacebookSuggestionParser() {
        http = new HttpUrlConnectionExample();
    }
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static String getName(String access, String picUrl, String user, String pass)           throws Exception {
        Log.d("Lee", "user" + user);
        Log.d("Lee", "url" + picUrl);
        Log.d("Lee", pass);
        String url = "https://www.facebook.com/login";
        String pic = picUrl;
        FacebookSuggestionParser parse = new FacebookSuggestionParser();
        // make sure cookies is turn on

        CookieHandler.setDefault(new CookieManager());

        // 1. Send a "GET" request, so that you can extract the form's data.
//        String page = parse.http.GetPageContent(url);
//        String postParams = parse.http.getFormParams(page, user, pass);
//       
//        
//
//        // 2. Construct above post's content and then send a POST request for
//        parse.http.sendPost(url, postParams);
//        // 3. success then go to picture.
        String result = parse.http.GetPageContent(pic + access);
        Log.d("Lee", "access" + access);
//        Document doc = Jsoup.parse(page);
//        Log.d("form", doc.body().html());
        
        
        System.out.println(result);
        Log.d("Lee", "done");
        Log.d("Lee", result);
        Log.d("Lee", "namefound" + parse.findName(result));
        parse.notifyAll();
        return (parse.findName(result));
    }
    public String findName(String parseMe) {
        String findMe = "_570u faceboxSuggestion";
        int start = parseMe.indexOf(findMe);
        if (start == -1) {
            return null;
        }
        int index = start += findMe.length();
        int i = 0;
        while (i < 2) { // Two white space before data-text
            if (parseMe.charAt(index) == ' ') {
                i++;
            }
            index++;
        }
        int begin = parseMe.indexOf('"', index) + 1; // Ignore quotation mark
        int end = parseMe.indexOf('"', begin);
        return parseMe.substring(begin, end);
    }
    public class HttpUrlConnectionExample {
        private List<String> cookies;
        private HttpURLConnection conn;
        private final String USER_AGENT = "Mozilla/5.0";
        
        private void sendPost(String url, String postParams) throws Exception {
            URL obj = new URL(url);
            conn = (HttpURLConnection) obj.openConnection();
            // Acts like a browser
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Host", "www.facebook.com");
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            for (String cookie : this.cookies) {
                Log.d("cookiepost", cookie);
                conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }
            conn.setRequestProperty("Referer", "https://www.facebook.com/login");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");           
            conn.setFixedLengthStreamingMode(postParams.getBytes().length); 

//            conn.setRequestProperty("Content-Length",Integer.toString(postParams.length()));           
            conn.setDoOutput(true);
            conn.setDoInput(true);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + postParams);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in =  new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
        }
        private String GetPageContent(String url) throws Exception {
//            System.setProperty("http.keepAlive", "false");
            URL obj = new URL(url);
            conn = (HttpURLConnection) obj.openConnection();
//            conn.setConnectTimeout(5000);
            // default is GET
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            // act like a browser
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            conn.setDoOutput(false);
            conn.setDoInput(true);
            if (cookies != null) {
                for (String cookie : this.cookies) {
                    Log.d("cookieget", cookie);
                    conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
                }
            }
            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // Get the response cookies
            setCookies(conn.getHeaderFields().get("Set-Cookie"));
            for(String c : getCookies()){
                Log.d("getcookies", c);
            }
            return response.toString();
        }
        public String getFormParams(String html, String username,String password)
                throws UnsupportedEncodingException {
            System.out.println("Extracting form's data...");
            Document doc = Jsoup.parse(html);
            Log.d("form", doc.body().html());

            // Google form id
            Element loginform = doc.getElementById("loginform");
            Elements inputElements = loginform.getElementsByTag("input");
            List<String> paramList = new ArrayList<String>();
            for (Element inputElement : inputElements) {
                String key = inputElement.attr("name");
                String value = inputElement.attr("value");
                if (key.equals("email"))
                    value = username;
                else if (key.equals("pass"))
                    value = password;
                paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
            }
            // build parameters list
            StringBuilder result = new StringBuilder();
            for (String param : paramList) {
                if (result.length() == 0) {
                    result.append(param);
                } else {
                    result.append("&" + param);
                }
            }
            Log.d("Lee", result.toString());
            return "lsd=AVrG6UzU&display=&enable_profile_selector=&legacy_return=1&profile_selector_ids=&trynum=1&timezone=300&lgnrnd=075851_Ztgo&lgnjs=1405695531&email=lweisberger5%40gmail.com&pass=T%21conderoga&default_persistent=0";
        }
        public List<String> getCookies() {
            return cookies;
        }
        public void setCookies(List<String> cookies) {
            this.cookies = cookies;
        }
    }
    @Override   protected String doInBackground(String... params) {
        
            String access = params[0];
            String url = params[1];
            String user = params[2];
            String pass = params[3];
            try {
                return getName(access,url, user, pass);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        
    }
//    public static void main(String[] args) {   
//        try {   
//            FacebookSuggestionParser.getName("https://www.facebook.com/photo.php?fbid=10204349279916346&set=p.10204349279916346&type=1", "lweisberger5@gmail.com", "T!conderoga");
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

}