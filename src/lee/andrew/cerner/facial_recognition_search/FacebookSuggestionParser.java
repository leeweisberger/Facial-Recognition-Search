package lee.andrew.cerner.facial_recognition_search;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FacebookSuggestionParser {

    HttpUrlConnectionExample http;

    public FacebookSuggestionParser() {

        http = new HttpUrlConnectionExample();

    }

    public static HashMap<String, String> getInfo(String picUrl, String timelineRedirectUrl,
            String user, String pass) throws Exception {
        HashMap<String, String> result = new HashMap<String, String>();

        String url = "https://www.facebook.com/login";

        String pic = picUrl;

        String timelineRedirect = timelineRedirectUrl;

        FacebookSuggestionParser parse = new FacebookSuggestionParser();

        // make sure cookies is turn on

        CookieHandler.setDefault(new CookieManager());

        // 1. Send a "GET" request, so that you can extract the form's data.

        String page = parse.http.GetPageContent(url);

        String postParams = parse.http.getFormParams(page, user, pass);

        // 2. Construct above post's content and then send a POST request for

        // authentication

        parse.http.sendPost(url, postParams);

        // 3. success then go to picture.

        String namePage = parse.http.GetPageContent(pic);

        String nameCheck = parse.findName(namePage);
        
        if(nameCheck == null) {
            return null;
        }
        System.out.println("Name: " + nameCheck);
        result.put("name", nameCheck);
        
        // 4. go to friend's list       
        String friendListPage = parse.http.GetPageContent(parse.getFriendsUrl(timelineRedirect, parse.http));
        
        // 5. go to about page of identified person
        System.out.println("Step 5");
        String friendAboutUrl = "https://www.facebook.com/" + parse.findFriendPage(result.get("name"), friendListPage) + "/about";
        System.out.println("friendAboutUrl: " + friendAboutUrl);
        Document friendAbout = (Document) Jsoup.parse(parse.http.GetPageContent(friendAboutUrl));
        parse.addBasicInfo(friendAbout, result);
        
        return result;

    }
    
    private void addBasicInfo(Document friendInfo, HashMap<String, String> result) {
        ArrayList<String> keys = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();
        //System.out.println("friendInfo: " + friendInfo.toString());
        String whyMark = friendInfo.toString().replaceAll("<!--", "");
        whyMark = whyMark.replaceAll("-->", "");
        
        Document fuckYouMark = (Document) Jsoup.parse(whyMark);
        Elements hidden = fuckYouMark.getElementsByClass("hidden_elem");
        //System.out.println("hidden: " + hidden.toString());
        Element info = searchThroughShit(hidden);
        System.out.println();
        System.out.println("info: " + info);
        if(info == null)
            return;
        Elements friendBasicInfoKeys = info.getElementsByClass("_3sts");
        Elements friendBasicInfoValues = info.getElementsByClass("_480u");
        for(Element e: friendBasicInfoKeys) {
            keys.add(e.text());
            System.out.println("Key added! " + e.text());
        }
        for(Element e: friendBasicInfoValues) {
            values.add(e.text());
            System.out.println("Value added! " + e.text());
        }
        for(int i=0; i<keys.size(); i++) {
            result.put(keys.get(i), values.get(i));
        }
    }
    
    private Element searchThroughShit(Elements shit) {
        for(Element e: shit) {
            if(e.toString().contains("Interested In")) {
                return e;
            }
        }
        return null;
    }
    
    private String getFriendsUrl(String timelineRedirect, HttpUrlConnectionExample help) throws Exception {
        Document parseMe = (Document) Jsoup.parse(help.GetPageContent(timelineRedirect));
        Elements test = parseMe.getElementsByAttributeValue("http-equiv", "refresh");
        System.out.println("Document test: " + test.attr("content"));
        Document actualParseMe = (Document) Jsoup.parse(help.GetPageContent(parseFriend(test.attr("content"))));
        //System.out.println("ActualParseMe: " + actualParseMe.toString());
        Elements tabs = parseMe.getElementsByClass("hidden_elem");
        //System.out.println("tabs: " + tabs.first());
        //Elements friendAttr = getElement(tabs, "_70k").getElementsByAttributeValue("data-medley-id", "pagelet_timeline_medley_friends"); 
        //System.out.println("Returning: " + friendAttr.first().attr("data-medley-id"));
        System.out.println("Returning: " + parse_70k(tabs.toString()));
        return parse_70k(tabs.toString());
    }
    
    private String parse_70k(String hidden_elem) {
        int i = hidden_elem.indexOf("_70k");
        int j = hidden_elem.indexOf("/friends", i);
        while(hidden_elem.charAt(j) != '"') {
            j++;
        }
        int x = j;
        j--;
        while(hidden_elem.charAt(j) != '"') {
            j--;
        }
        return hidden_elem.substring(j+1, x);
    }
    
    private Element getElement(Elements haystack, String Class) {
        for(int i=0; i<haystack.size(); i++) {
            //System.out.println(haystack.get(i));
            if(haystack.get(i).hasAttr(Class)) {
                return haystack.get(i);
            }
        }
        return null;
    }

    private String parseFriend(String content) {
        int i = content.indexOf('/');
        int j = content.indexOf('?');
        return "https://www.facebook.com" + content.substring(i, j);
    }
    
    private String findFriendPage(String friendName, String friendListPage) {
        String findMe = convertName(friendName);
        //System.out.println("Finding index of " + findMe);
        int i = friendListPage.indexOf(findMe);
        //System.out.println("Found it! " + i);
        char temp = ' ';
        int j = i;
        while(temp != '"') {
            j--;
            temp = friendListPage.charAt(j);
        }
        int x = i;
        temp = ' ';
        while(temp != '/') {
            x++;
            temp = friendListPage.charAt(x);
        }
        System.out.println("findFriendPage return: " + friendListPage.substring(i, x));
        return friendListPage.substring(i, x);
    }
    
    private String convertName(String name) {
        int i = name.indexOf(' ');
        String first = name.substring(0, i);
        String second = name.substring(i+1, name.length());
        return first.toLowerCase() + "." + second.toLowerCase();
    }
    
    private String findName(String parseMe) {

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

            conn.setUseCaches(true);

            conn.setRequestMethod("POST");

            conn.setRequestProperty("Host", "www.facebook.com");

            conn.setRequestProperty("User-Agent", USER_AGENT);

            conn.setRequestProperty("Accept",

            "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            for (String cookie : this.cookies) {

                conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);

            }
            

            conn.setRequestProperty("Referer", "https://www.facebook.com/login");

            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            conn.setRequestProperty("Content-Length",
                    Integer.toString(postParams.length()));

            conn.setDoOutput(true);

            conn.setDoInput(true);

            // Send post requestfa

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

            wr.writeBytes(postParams);

            wr.flush();

            wr.close();

            int responseCode = conn.getResponseCode();

            System.out.println("\nSending 'POST' request to URL : " + url);

            System.out.println("Post parameters : " + postParams);

            System.out.println("Response Code : " + responseCode);

            BufferedReader in =

            new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;

            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {

                response.append(inputLine);

            }

            in.close();

            //System.out.println(response.toString());

        }

        private String GetPageContent(String url) throws Exception {

            System.setProperty("http.keepAlive", "false");

            URL obj = new URL(url);

            conn = (HttpURLConnection) obj.openConnection();

            conn.setConnectTimeout(5000);

            // default is GET

            conn.setRequestMethod("GET");

            conn.setUseCaches(false);

            // act like a browser

            conn.setRequestProperty("User-Agent", USER_AGENT);

            conn.setRequestProperty("Accept",

            "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            if (cookies != null) {

                for (String cookie : this.cookies) {

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

            return response.toString();

        }

        public String getFormParams(String html, String username,
                String password)

        throws UnsupportedEncodingException {

            System.out.println("Extracting form's data...");

            Document doc = Jsoup.parse(html);

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

            return result.toString();

        }

        public List<String> getCookies() {

            return cookies;

        }

        public void setCookies(List<String> cookies) {

            this.cookies = cookies;

        }

    }

}