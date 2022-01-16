package App;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

public class SimpleWeather {

    private JsonElement jse;
    private JsonElement mapBox;
    private JsonElement cityState;
    private String zipCode;
    int day;

    public SimpleWeather(String k)
    {
        zipCode = k;
    } //zip = ":auto"

    public static String urlEncode(String str)
    {
        String encoded = "";
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (c == ' ') encoded += "%20";
            else if (c >= 'a' && c <= 'z') encoded += c;
            else if (c >= '0' && c <= '9') encoded += c;
            else if (c == '.' || c == '-' || c == '_' || c == '~') encoded += c;
            else encoded += "%" + Integer.toString(c, 16);
        }
        return encoded;
    }

    public void fetch()
    {
        // https://api.aerisapi.com/observations/95677?client_id=CLIENT_ID&client_secret=CLIENT_SECRET
        //https://api.aerisapi.com/forecasts/95677?client_id=CLIENT_ID&client_secret=CLIENT_SECRET
        String urlString = "https://api.aerisapi.com/forecasts/"
                + urlEncode(zipCode)
                //+ "?client_id=CF0efkpGXmI6VjS5CP7wO&client_secret=qQqWhVMgh9MJNkSjDk0XTwkc25Ps9TrwNz6wSGHN";
                + "?client_id=OTzYARUL1FlXArE8Gm9LQ&client_secret=JCSoFxlqO5mfyEKWXg45tiJnYgwi9WL5SeEMViu0";
        System.out.println(urlString);
        try {
            URL url = new URL(urlString);

            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            jse = JsonParser.parseReader(br);
            System.out.println(jse);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cityName(){
        // https://api.aerisapi.com/observations/95677?client_id=CLIENT_ID&client_secret=CLIENT_SECRET
        //https://api.aerisapi.com/forecasts/95677?client_id=CLIENT_ID&client_secret=CLIENT_SECRET
        String urlString = "https://api.aerisapi.com/observations/"
                + urlEncode(zipCode)
                //+ "?client_id=CF0efkpGXmI6VjS5CP7wO&client_secret=qQqWhVMgh9MJNkSjDk0XTwkc25Ps9TrwNz6wSGHN";
                + "?client_id=OTzYARUL1FlXArE8Gm9LQ&client_secret=JCSoFxlqO5mfyEKWXg45tiJnYgwi9WL5SeEMViu0";
        System.out.println(urlString);
        try {
            URL url = new URL(urlString);

            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            cityState = JsonParser.parseReader(br);
            System.out.println(jse);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getTempF(int day){
        return jse.getAsJsonObject().get("response").getAsJsonArray().get(0).
                getAsJsonObject().get("periods").getAsJsonArray().get(day).
                getAsJsonObject().get("avgTempF").getAsDouble();
        //return jse.getAsJsonObject().get("response").getAsJsonObject().get("ob").getAsJsonObject().get("tempF").getAsDouble();
    }

    public double getTempC(int day){
        return jse.getAsJsonObject().get("response").getAsJsonArray().get(0).
                getAsJsonObject().get("periods").getAsJsonArray().get(day).
                getAsJsonObject().get("avgTempC").getAsDouble();
    }

    public double getHeatindexF(int day){
        return jse.getAsJsonObject().get("response").getAsJsonArray().get(0).
                getAsJsonObject().get("periods").getAsJsonArray().get(day).
                getAsJsonObject().get("maxTempF").getAsDouble();
    }

    public double getHeatindexC(int day){
        return jse.getAsJsonObject().get("response").getAsJsonArray().get(0).
                getAsJsonObject().get("periods").getAsJsonArray().get(day).
                getAsJsonObject().get("maxTempC").getAsDouble();
    }

    public double getDewpointF(int day){
        return jse.getAsJsonObject().get("response").getAsJsonArray().get(0).
                getAsJsonObject().get("periods").getAsJsonArray().get(day).
                getAsJsonObject().get("minTempF").getAsDouble();
    }

    public double getDwpointC(int day){
        return jse.getAsJsonObject().get("response").getAsJsonArray().get(0).
                getAsJsonObject().get("periods").getAsJsonArray().get(day).
                getAsJsonObject().get("minTempC").getAsDouble();
    }

    public String getWeather(int day){
        return jse.getAsJsonObject().get("response").getAsJsonArray().get(0).
                getAsJsonObject().get("periods").getAsJsonArray().get(day).
                getAsJsonObject().get("weatherPrimary").getAsString();
    }

    public String getState(){
        //cityName();

        return cityState.getAsJsonObject().get("response").getAsJsonObject().get("place").getAsJsonObject().get("state").getAsString();
    }

    public String getCity(){
        //cityName();

        return cityState.getAsJsonObject().get("response").getAsJsonObject().get("place").getAsJsonObject().get("name").getAsString();
    }

    public String getIcon(int day){
        return jse.getAsJsonObject().get("response").getAsJsonArray().get(0).
                getAsJsonObject().get("periods").getAsJsonArray().get(day).
                getAsJsonObject().get("icon").getAsString();
    }

    //check if error is null
    public boolean ifCity(){
        JsonElement j = cityState.getAsJsonObject().get("error");
        return j.isJsonNull();
    }

    public String errorMessage(){
        return cityState.getAsJsonObject().get("error").getAsJsonObject().get("description").getAsString();
    }

    public String getDate(int day){
        return jse.getAsJsonObject().get("response").getAsJsonArray().get(0).
                getAsJsonObject().get("periods").getAsJsonArray().get(day).
                getAsJsonObject().get("dateTimeISO").getAsString();
    }
    public String getCityName(){
        String city = "";
        for(int i = 0; i <10 ; i++ ) {
            city = jse.getAsJsonObject().get("features").getAsJsonArray().get(i).getAsJsonObject().get("text").getAsString();
        }
        return city;
    }

    public Image getRadar(){
        try{
            String URL = "https://maps.aerisapi.com/OTzYARUL1FlXArE8Gm9LQ_JCSoFxlqO5mfyEKWXg45tiJnYgwi9WL5SeEMViu0/flat-dk,radar,admin-cities-dk,water-flat,ftemperatures-max-text/256x256/"
                    + URLEncoder.encode(getLongLat(), "utf-8") + ",8/current.png";
            Image i = new Image(URL);
            return i;
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getLongLat(){
        double lon = jse.getAsJsonObject().get("response").getAsJsonArray().get(0).getAsJsonObject().get("loc").getAsJsonObject().get("long").getAsDouble();
        double lat = jse.getAsJsonObject().get("response").getAsJsonArray().get(0).getAsJsonObject().get("loc").getAsJsonObject().get("lat").getAsDouble();
        return lat + "," + lon;
    }

    public String getZip(){
        return zipCode;
    }

    public static void main(String[] args)
    {
        SimpleWeather w = new SimpleWeather("95762");
        w.fetch();

        System.out.println(w.getLongLat());
        //System.out.println(w.getCity());
    }


}
