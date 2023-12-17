package ru.kpfu.itis.itisorissemesterwork2nikolaev;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CongratulationsController {

    @FXML
    private Button buttonExit;

    @FXML
    void onClickMain(ActionEvent event) throws IOException {
        Parent wow = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/profile.fxml")));
        Scene scene = new Scene(wow);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
