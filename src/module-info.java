module WeatherAppInGroups {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    opens App;
    requires asynctask;
}