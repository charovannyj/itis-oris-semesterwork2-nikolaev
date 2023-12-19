package ru.kpfu.itis.itisorissemesterwork2nikolaev;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ProfileController {

    @FXML
    private Button buttonExit;

    @FXML
    private Text textAeroflotBuy;

    @FXML
    private Text textAeroflotSell;

    @FXML
    private Text textGoldBuy;

    @FXML
    private Text textGoldSell;

    @FXML
    private Text textRosneftBuy;

    @FXML
    private Text textRosneftSell;

    @FXML
    private Text textSber;

    @FXML
    private Text textSberBuy;

    @FXML
    private Text textSberSell;

    @FXML
    void onClickBack(ActionEvent event) throws IOException {
        Parent wow = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/main.fxml")));
        Scene scene = new Scene(wow);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void onClickSber(MouseEvent event) throws IOException {
        InvestmentsApplication.database.currentCompany = Company.SBER.value;
        Parent wow = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/chooseAction.fxml")));
        Scene scene = new Scene(wow);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void onClickRosneft(MouseEvent event) throws IOException {
        InvestmentsApplication.database.currentCompany = Company.ROSNEFT.value;
        Parent wow = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/chooseAction.fxml")));
        Scene scene = new Scene(wow);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void onClickAeroflot(MouseEvent event) throws IOException {
        InvestmentsApplication.database.currentCompany = Company.AEROFLOT.value;
        Parent wow = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/chooseAction.fxml")));
        Scene scene = new Scene(wow);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void onClickGold(MouseEvent event) throws IOException {
        InvestmentsApplication.database.currentCompany = Company.GOLD.value;
        Parent wow = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/chooseAction.fxml")));
        Scene scene = new Scene(wow);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void initialize() throws SQLException {
        textAeroflotSell.setText(String.valueOf(searchPrice("SELL_BID", "AEROFLOT")));
        textAeroflotBuy.setText(String.valueOf(searchPrice("BUY_BID","AEROFLOT")));
        textRosneftSell.setText(String.valueOf(searchPrice("SELL_BID", "ROSNEFT")));
        textRosneftBuy.setText(String.valueOf(searchPrice("BUY_BID","ROSNEFT")));
        textSberSell.setText(String.valueOf(searchPrice("SELL_BID", "SBER")));
        textSberBuy.setText(String.valueOf(searchPrice("BUY_BID","SBER")));
        textGoldSell.setText(String.valueOf(searchPrice("SELL_BID", "GOLD")));
        textGoldBuy.setText(String.valueOf(searchPrice("BUY_BID","GOLD")));


    }
    float searchPrice(String action, String company) throws SQLException {
        List<Float> pricec = new ArrayList<>();
        try {
            Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
            String sql = "SELECT price FROM offers WHERE company='" + company + "' AND action='" + action + "';";
            ResultSet resultSet = statement.executeQuery(sql);
            boolean ok = true;
            if (resultSet != null) {
                while (resultSet.next()) {
                    ok = false;
                    pricec.add(resultSet.getFloat("price"));
                }
            }
            if (ok) return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (action.equals("SELL_BID")) return Collections.min(pricec);
        else if(action.equals("BUY_BID")) return Collections.max(pricec);
        else return 0;
    }

}
