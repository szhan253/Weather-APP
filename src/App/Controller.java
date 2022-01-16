package App;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import sierra.AsyncTask;

public class Controller implements Initializable {
    @FXML
    public TextField zipField;

    public Button goButton;
    public Button tempC;
    public Button auto;

    public Label zipCode;
    public Label W;
    public Label T;
    public Label cAndS;

    public ImageView imageSpot;
    public ImageView radarImage;

    //forecast
    public Label errorM;
    public Label currentDewpointAndHeatindex;
    public Label Day2;
    public Label Day3;
    public Label Day4;
    public Label Day5;
    public Label Day6;
    public Label Day7;
    public ImageView imageSpot2;
    public ImageView imageSpot3;
    public ImageView imageSpot4;
    public ImageView imageSpot5;
    public ImageView imageSpot6;
    public ImageView imageSpot7;
    public Label T2, T3, T4, T5, T6, T7;
    /*
    public Label T3;
    public Label T4;
    public Label T5;
    public Label T6;
    public Label T7;
     */
    public Label W2;
    public Label W3;
    public Label W4;
    public Label W5;
    public Label W6;
    public Label W7;

    public ListView listView;

    private JsonElement mapBox;

    ArrayList<String> allCity;
    ArrayList<String> latLon;

    public List<Label> temps = new ArrayList<Label>(); //= {T2, T3, T4, T5, T6, T7};
    //temps[0] = T2;






    boolean isF = true;
    SimpleWeather w;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fetchData(":auto");

        temps.add(T2);
        temps.add(T3);
        temps.add(T4);
        temps.add(T5);
        temps.add(T6);
        temps.add(T7);// = {T2, T3, T4, T5, T6};




        listView.setVisible(false);
    }

    public void goButton(ActionEvent actionEvent) {
        /*
        String kw = keywordField.getText();
        News n = new News(kw);
        n.fetch();
        headlineField.setText(n.getTopHeadline());
         */

        String zipC = zipField.getText();

        fetchData(zipField.getText());
    }

    public void auto(ActionEvent actionEvent){
        fetchData(":auto");
    }

    //change temp between C and F
    public void setTemp(ActionEvent actionEvent){
        /*
        String zipC = zipField.getText();
        SimpleWeather w = new SimpleWeather(zipC);*/

        if (isF) {
            T.setText((w.getTempC(0)) + " ℃");
            currentDewpointAndHeatindex.setText(w.getHeatindexC(0) + " ℃ | " + w.getDwpointC(0) + " ℃");
            isF = false;
            tempC.setText("℉");
            /*
            T2.setText(w.getHeatindexC(1) + " ℃ | " + w.getDwpointC(1) + " ℃");
            T3.setText(w.getHeatindexC(2) + " ℃ | " + w.getDwpointC(2) + " ℃");
            T4.setText(w.getHeatindexC(3) + " ℃ | " + w.getDwpointC(3) + " ℃");
            T5.setText(w.getHeatindexC(4) + " ℃ | " + w.getDwpointC(4) + " ℃");
            T6.setText(w.getHeatindexC(5) + " ℃ | " + w.getDwpointC(5) + " ℃");
            T7.setText(w.getHeatindexC(6) + " ℃ | " + w.getDwpointC(6) + " ℃");

             */
            for(int i = 0; i < 6; i++ ){
                temps.get(i).setText(w.getHeatindexC(i) + " ℃ | " + w.getDwpointC(i) + " ℃");
            }
        }
        else {
            T.setText((w.getTempF(0)) + " ℉");
            currentDewpointAndHeatindex.setText(w.getHeatindexF(0) + " ℉ | " + w.getDewpointF(0) + " ℉");
            isF = true;
            tempC.setText("℃");
            T2.setText(w.getHeatindexF(1) + " ℉ | " + w.getDewpointF(1) + " ℉");
            T3.setText(w.getHeatindexF(2) + " ℉ | " + w.getDewpointF(2) + " ℉");
            T4.setText(w.getHeatindexF(3) + " ℉ | " + w.getDewpointF(3) + " ℉");
            T5.setText(w.getHeatindexF(4) + " ℉ | " + w.getDewpointF(4) + " ℉");
            T6.setText(w.getHeatindexF(5) + " ℉ | " + w.getDewpointF(5) + " ℉");
            T7.setText(w.getHeatindexF(6) + " ℉ | " + w.getDewpointF(6) + " ℉");
        }
    }

    public void fetchData(String zip) {
        String input = zipField.getText();
        AsyncTask t = new GetDataInBackground();
        t.execute(zip);
    }


    public void enterPress(KeyEvent keyEvent) {
        if (keyEvent.getCode().getCode() == KeyCode.ENTER.getCode()){
            goButton(null);
        }
    }

    public void click(MouseEvent mouseEvent) {
        String selected = (String) listView.getSelectionModel().getSelectedItem();
        if(selected.equals("")){
            return;
        }
        int i = allCity.indexOf(selected);
        fetchData(latLon.get(i));

        listView.setVisible(false);
        zipField.setText("");
    }

    public void inputChange(KeyEvent keyEvent){
        String name = zipField.getText();

        allCity = new ArrayList<String>();
        latLon = new ArrayList<String>();

        if(name.equals("")){
            listView.setVisible(false);
            return;
        }

        if(!Character.isAlphabetic(name.charAt(0))){
            listView.setVisible(false);
            return;
        }

        listView.setVisible(true);

        //https://api.mapbox.com/geocoding/v5/mapbox.places/QUERY.json?access_token=API_KEY&limit=10&types=place
        String urlString = "https://api.mapbox.com/geocoding/v5/mapbox.places/"+
                SimpleWeather.urlEncode(name)
                +".json?access_token="  + "pk.eyJ1Ijoic2llcnJhY3MiLCJhIjoiY2p2YWExN3NwMGQ3aTQxbzAxZnh6YzloMiJ9.BL6OLkhv0rHoKtMnL_kEkA" + "&limit=10&types=place";
        System.out.println(urlString);
        try {
            URL url = new URL(urlString);

            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            mapBox = JsonParser.parseReader(br);
            System.out.println(mapBox);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonArray locationArray = mapBox.getAsJsonObject().get("features").getAsJsonArray();
        int n = locationArray.size();



        for (int i = 0; i < n; i ++){
            allCity.add(locationArray.get(i).getAsJsonObject().get("place_name").getAsString());

            Double lat = locationArray.get(i).getAsJsonObject().get("center").getAsJsonArray().get(1).getAsDouble();
            Double lon = locationArray.get(i).getAsJsonObject().get("center").getAsJsonArray().get(0).getAsDouble();

            latLon.add(lat + "," + lon);

        }

        ObservableList l = FXCollections.observableArrayList(allCity);
        listView.setItems(l);

    }

    private class GetDataInBackground extends AsyncTask<String, SimpleWeather>
    {
        @Override
        public SimpleWeather doInBackground(String zip)
        {
            // Fetch the weather data
            w = new SimpleWeather(zip);
            errorM.setVisible(false);
            w.cityName();
            w.fetch();

            return w;
        }

        @Override
        public void onPostExecute(SimpleWeather w)
        {
            // Update the data on the screen
            //change if icon and forecast are available base on if error is null.
            //if there no data can be used, just hide the forecast
            tempC.setVisible(w.ifCity());
            imageSpot.setVisible(w.ifCity());
            currentDewpointAndHeatindex.setVisible(w.ifCity());
            Day2.setVisible(w.ifCity());
            Day3.setVisible(w.ifCity());
            Day4.setVisible(w.ifCity());
            Day5.setVisible(w.ifCity());
            Day6.setVisible(w.ifCity());
            Day7.setVisible(w.ifCity());
            imageSpot2.setVisible(w.ifCity());
            imageSpot3.setVisible(w.ifCity());
            imageSpot4.setVisible(w.ifCity());
            imageSpot5.setVisible(w.ifCity());
            imageSpot6.setVisible(w.ifCity());
            imageSpot7.setVisible(w.ifCity());
            T2.setVisible(w.ifCity());
            T3.setVisible(w.ifCity());
            T4.setVisible(w.ifCity());
            T5.setVisible(w.ifCity());
            T6.setVisible(w.ifCity());
            T7.setVisible(w.ifCity());
            W2.setVisible(w.ifCity());
            W3.setVisible(w.ifCity());
            W4.setVisible(w.ifCity());
            W5.setVisible(w.ifCity());
            W6.setVisible(w.ifCity());
            W7.setVisible(w.ifCity());

            if (w.getZip().isEmpty() == true) {

                W.setText("----");
                T.setText("----");
                cAndS.setText("----");

                errorM.setVisible(true);
                errorM.setText("Please enter a Zip Code or a City Name.");
            } else {

                if (w.ifCity() == true) {
                    //get city and state
                    //w.cityName();
                    cAndS.setText(w.getCity() + " , " + w.getState());
                    //w.cityName();
                    //w.fetch();

                    //for(int day = 0; day < 7; day ++) {
                    //get weather
                    W.setText(w.getWeather(0));
                    //get temp
                    T.setText((w.getTempF(0)) + "℉");

                    //get weather icon
                    Image icon = new Image(w.getIcon(0));
                    imageSpot.setImage(icon);

                    //get radar
                    radarImage.setImage(w.getRadar());

                    String dateStr = w.getDate(0);
                    dateStr = dateStr.substring(0, 10);
                    LocalDate day = LocalDate.parse(dateStr);
                    int t = day.getDayOfWeek().getValue();

                    String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

                    Day2.setText(days[(t + 1) % 7]);
                    Day3.setText(days[(t + 2) % 7]);
                    Day4.setText(days[(t + 3) % 7]);
                    Day5.setText(days[(t + 4) % 7]);
                    Day6.setText(days[(t + 5) % 7]);
                    Day7.setText(days[(t + 6) % 7]);


                    currentDewpointAndHeatindex.setText(w.getHeatindexF(0) + " ℉ | " + w.getDewpointF(0) + " ℉");

                    imageSpot2.setImage(new Image(w.getIcon(1)));
                    imageSpot3.setImage(new Image(w.getIcon(2)));
                    imageSpot4.setImage(new Image(w.getIcon(3)));
                    imageSpot5.setImage(new Image(w.getIcon(4)));
                    imageSpot6.setImage(new Image(w.getIcon(5)));
                    imageSpot7.setImage(new Image(w.getIcon(6)));
                    T2.setText(w.getHeatindexF(1) + " ℉ | " + w.getDewpointF(1) + " ℉");
                    T3.setText(w.getHeatindexF(2) + " ℉ | " + w.getDewpointF(2) + " ℉");
                    T4.setText(w.getHeatindexF(3) + " ℉ | " + w.getDewpointF(3) + " ℉");
                    T5.setText(w.getHeatindexF(4) + " ℉ | " + w.getDewpointF(4) + " ℉");
                    T6.setText(w.getHeatindexF(5) + " ℉ | " + w.getDewpointF(5) + " ℉");
                    T7.setText(w.getHeatindexF(6) + " ℉ | " + w.getDewpointF(6) + " ℉");
                    W2.setText(w.getWeather(1));
                    W3.setText(w.getWeather(2));
                    W4.setText(w.getWeather(3));
                    W5.setText(w.getWeather(4));
                    W6.setText(w.getWeather(5));
                    W7.setText(w.getWeather(6));
                } else {
                    errorM.setVisible(true);
                    errorM.setText(w.errorMessage());

                    W.setText("----");
                    T.setText("----");
                    cAndS.setText("----");

                }

            }
            radarImage.setVisible(w.ifCity());
        }
    }

}