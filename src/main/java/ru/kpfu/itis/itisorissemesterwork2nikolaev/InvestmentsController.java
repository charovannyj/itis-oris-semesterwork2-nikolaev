package ru.kpfu.itis.itisorissemesterwork2nikolaev;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.dao.impl.OfferDaoImpl;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.model.Offer;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.model.User;

public class InvestmentsController {
    @FXML
    private Text text;
    @FXML
    private Button button;
    @FXML
    private Label label;
    private int sumRubles = 10000;
    @FXML
    private TextField textFieldForName;

    @FXML
    void onClickEnter(ActionEvent event) throws IOException {

    }

    private static final Connection connection = DatabaseConnectionUtil.getConnection();

    @FXML
    void initialize() {

    }

    @FXML
    void onClickStartField(ActionEvent event) {

    }

    public static boolean isExistUser(String name) {
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM public.users WHERE name='" + name + "';";
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void signInOrUpUser() {
        User user = null;
        if (!isExistUser(InvestmentsApplication.database.currentName)) {
            String query = "INSERT INTO public.users (name, sumrubles, sumsber, sumrosneft,sumaeroflot,sumgold) VALUES (?, ?, ?, ?, ?, ?);";
            try (PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(query)) {
                preparedStatement.setString(1, InvestmentsApplication.database.currentName);
                preparedStatement.setInt(2, 10000);
                preparedStatement.setInt(3, 0);
                preparedStatement.setInt(4, 0);
                preparedStatement.setInt(5, 0);
                preparedStatement.setInt(6, 0);
                preparedStatement.execute();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * from public.users WHERE name='" + InvestmentsApplication.database.currentName + "';" ;
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    user = new User(
                        resultSet.getString("name"),
                        resultSet.getInt("sumrubles"),
                        resultSet.getInt("sumsber"),
                        resultSet.getInt("sumrosneft"),
                        resultSet.getInt("sumaeroflot"),
                        resultSet.getInt("sumgold")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        InvestmentsApplication.database.currentUser = user;
    }
}

