package ru.kpfu.itis.itisorissemesterwork2nikolaev;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class InvestmentsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void onClickEnter(ActionEvent event) throws IOException {
        Parent wow = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/profile.fxml")));
        Scene scene = new Scene(wow);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() {

    }

}
