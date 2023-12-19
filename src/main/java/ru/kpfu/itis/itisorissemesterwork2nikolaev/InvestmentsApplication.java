package ru.kpfu.itis.itisorissemesterwork2nikolaev;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.model.User;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.tcp.Client;

import java.io.IOException;
import java.util.Objects;

import static ru.kpfu.itis.itisorissemesterwork2nikolaev.InvestmentsController.signInOrUpUser;

public class InvestmentsApplication extends Application {
    public static Database database;
    private static Client client; // Добавляем поле для экземпляра класса Client

    public static Client getClient() {
        return client;
    }

    @Override
    public void start(Stage stage) throws IOException {
        database = new Database();
        client = new Client("127.0.0.1", 5555);

        AnchorPane anchorPane = new AnchorPane();
        FXMLLoader loader = new FXMLLoader(InvestmentsApplication.class.getResource("fxml/main.fxml"));
        anchorPane.getChildren().add(loader.load());

        Button loginButton = (Button) anchorPane.lookup("#button");
        loginButton.setOnAction(e -> {
            TextField usernameField = (TextField) anchorPane.lookup("#textFieldForName");
            String username = usernameField.getText();
            User user = client.loginUser(username);
            database.currentUser = user;
            database.currentUser.name = username;
            try {
                loadNewScene(e, "profile");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        Scene k = new Scene(anchorPane);
        stage.setTitle("INVESTMENTS");
        stage.setScene(k);
        stage.show();
    }

    void loadNewScene(ActionEvent event, String sceneName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/" + sceneName + ".fxml"));
        Parent root = loader.load();
        ProfileController controller = loader.getController();
        controller.setClient(client);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}