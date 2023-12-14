package ru.kpfu.itis.itisorissemesterwork2nikolaev;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        AnchorPane anchorPane = new AnchorPane();

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        anchorPane.getChildren().add(loader.load());

        Button btn = new Button();
        btn.setText("Click!");
        anchorPane.getChildren().add(btn);

        Scene k = new Scene(anchorPane);
        stage.setTitle("Hello!");
        stage.setScene(k);
        stage.show();
    }

}