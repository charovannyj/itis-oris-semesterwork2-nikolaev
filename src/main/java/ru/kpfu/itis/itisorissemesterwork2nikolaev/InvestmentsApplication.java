package ru.kpfu.itis.itisorissemesterwork2nikolaev;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class InvestmentsApplication extends Application {
    public static Database database;

    @Override
    public void start(Stage stage) throws IOException {
        database = new Database();
        AnchorPane anchorPane = new AnchorPane();

        FXMLLoader loader = new FXMLLoader(InvestmentsApplication.class.getResource("fxml/main.fxml"));
        anchorPane.getChildren().add(loader.load());

        Scene k = new Scene(anchorPane);
        stage.setTitle("INVESTMENTS");
        stage.setScene(k);
        stage.show();
    }

}