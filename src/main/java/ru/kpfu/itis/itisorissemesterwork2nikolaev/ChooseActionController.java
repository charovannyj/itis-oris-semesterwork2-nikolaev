package ru.kpfu.itis.itisorissemesterwork2nikolaev;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ChooseActionController {

    @FXML
    private ToggleGroup action;

    @FXML
    private Label errorLabel;

    @FXML
    private Button executeButton;

    @FXML
    void onClickExecute(ActionEvent event) throws IOException {
        double balance = Math.random();
        if (balance > 0.5){
            Parent wow = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/congratulations.fxml")));
            Scene scene = new Scene(wow);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
        else{
            errorLabel.setText("у вас недостаточно денег");
        }
    }

}
