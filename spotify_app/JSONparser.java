import org.json.*;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class JSONparser {

    /* public void test() throws Exception {
        //if (Desktop.isDesktopSupported()) {
        //    Desktop.getDesktop().browse(new URI("http://localhost:8888"));
        //
        String url = "https://api.spotify.com/v1/me/tracks?offset=0&limit=50";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        String token = "BQB3xDPDXaOC74lB3FvntnjjO1NTSX9AClR2S19-ECp-enbeOg46s_E-ZIeYuU2EJEovv0Z1abyWKeWk8AKxwuWY-DwgwQa_GVLsLBZEoDDUy_Ym8vbWTuH6TH1NQMqtMSkI_2j5HZN03kXLJdV0ST8EnTfE6Iaf7eiyhO2zlg";
        con.setRequestProperty("Authorization", "Bearer " + token);
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print in String
        System.out.println(response.toString());
        //Read JSON response and print
        JSONObject myResponse = new JSONObject(response.toString());
        JSONArray items = (JSONArray) myResponse.get("items");


        String url2 = "https://api.spotify.com/v1/me";
        URL obj2 = new URL(url2);
        HttpURLConnection con2 = (HttpURLConnection) obj2.openConnection();
        // optional default is GET
        con2.setRequestMethod("GET");
        //add request header
        con2.setRequestProperty("Authorization", "Bearer " + token);
        int responseCode2 = con2.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url2);
        System.out.println("Response Code : " + responseCode2);
        BufferedReader in2 = new BufferedReader(
                new InputStreamReader(con2.getInputStream()));
        String inputLine2;
        StringBuffer response2 = new StringBuffer();
        while ((inputLine2 = in2.readLine()) != null) {
            response2.append(inputLine2);
        }
        in2.close();
        //print in String
        System.out.println(response2.toString());
        //Read JSON response and print
        JSONObject myResponse2 = new JSONObject(response2.toString());

        UserLibrary currentUser = new UserLibrary(myResponse2, items);
        currentUser.writeToFile();
        /*
        String[] trackNames = new String[items.length()];
        for (int i = 0; i < items.length(); i++) {
            JSONObject trackInfo = (JSONObject) items.get(i);
            JSONObject track = (JSONObject) trackInfo.get("track");
            String trackName = (String) track.get("name");
            trackNames[i] = trackName;
        }
        for (int i = 0; i < trackNames.length; i++) {
            System.out.println(trackNames[i]);
        }
    } */

    public void itemMergeTest(String token) throws Exception {
        Integer offset = 0;
        Integer limit = 50;
        String url = "https://api.spotify.com/v1/me/tracks?offset=" + offset.toString() + "&limit=" + limit.toString();
        JSONObject myResponse = makeGetRequest(url, token);
        JSONArray items = (JSONArray) myResponse.get("items");
        Integer total_items = (Integer) myResponse.get("total");
        int numPages = Math.floorDiv(total_items - 1, limit) + 1;
        int pageCount = 0;
        JSONArray[] itemsArray = new JSONArray[numPages];
        while (pageCount < numPages) {
            url = "https://api.spotify.com/v1/me/tracks?offset=" + offset.toString() + "&limit=" + limit.toString();
            myResponse = makeGetRequest(url, token);
            JSONArray newItems = (JSONArray) myResponse.get("items");
            itemsArray[pageCount] = new JSONArray();
            for (int i = 0; i < newItems.length(); i++) {
                itemsArray[pageCount].put(newItems.get(i));
            }
            offset += limit;
            pageCount += 1;
        }
        String userURL = "https://api.spotify.com/v1/me";
        JSONObject user = makeGetRequest(userURL, token);
        UserLibrary MyLibrary = new UserLibrary(user, itemsArray);
        MyLibrary.writeToFile();

    }

    private JSONObject makeGetRequest(String url, String token) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("Authorization", "Bearer " + token);
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print in String
        System.out.println(response.toString());
        //Read JSON response and print
        JSONObject myResponse = new JSONObject(response.toString());
        return myResponse;
    }

    public UserLibrary getLibrary(String path) {

    }

}
