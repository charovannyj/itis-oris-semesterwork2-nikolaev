package ru.kpfu.itis.itisorissemesterwork2nikolaev;

import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.dao.impl.OfferDaoImpl;

import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class ChooseActionController implements Initializable {
    @FXML
    private ToggleGroup action;

    @FXML
    private Button buttonExit;

    @FXML
    private Label errorLabel;

    @FXML
    private Button executeButton;

    @FXML
    private RadioButton rb_bid_buy;

    @FXML
    private RadioButton rb_bid_sell;

    @FXML
    private RadioButton rb_buy;

    @FXML
    private RadioButton rb_sell;

    @FXML
    private TextField textFieldForPrice;

    @FXML
    private TextField textFieldForQuantity;

    @FXML
    private Label textForBid;

    @FXML
    private Label textSumRubles;

    @FXML
    private Text text_1_1;

    @FXML
    private Text text_1_10;

    @FXML
    private Text text_1_11;

    @FXML
    private Text text_1_12;

    @FXML
    private Text text_1_2;

    @FXML
    private Text text_1_3;

    @FXML
    private Text text_1_4;

    @FXML
    private Text text_1_5;

    @FXML
    private Text text_1_6;

    @FXML
    private Text text_1_7;

    @FXML
    private Text text_1_8;

    @FXML
    private Text text_1_9;

    @FXML
    private Text text_2_1;

    @FXML
    private Text text_2_10;

    @FXML
    private Text text_2_11;

    @FXML
    private Text text_2_12;

    @FXML
    private Text text_2_2;

    @FXML
    private Text text_2_3;

    @FXML
    private Text text_2_4;

    @FXML
    private Text text_2_5;

    @FXML
    private Text text_2_6;

    @FXML
    private Text text_2_7;

    @FXML
    private Text text_2_8;

    @FXML
    private Text text_2_9;

    @FXML
    private Text text_3_1;

    @FXML
    private Text text_3_10;

    @FXML
    private Text text_3_11;

    @FXML
    private Text text_3_12;

    @FXML
    private Text text_3_2;

    @FXML
    private Text text_3_3;

    @FXML
    private Text text_3_4;

    @FXML
    private Text text_3_5;

    @FXML
    private Text text_3_6;

    @FXML
    private Text text_3_7;

    @FXML
    private Text text_3_8;

    @FXML
    private Text text_3_9;


    @FXML
    void onClickBack(ActionEvent event) throws IOException {
        Parent wow = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/profile.fxml")));
        Scene scene = new Scene(wow);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onClickExecute(ActionEvent event) throws IOException, SQLException {

        InvestmentsApplication.database.currentQuantity = Integer.parseInt(textFieldForQuantity.getText());
        InvestmentsApplication.database.currentIdUser = InvestmentsApplication.database.currentUser.getId();


        if (InvestmentsApplication.database.currentAction.equals("BUY_BID") &&
                InvestmentsApplication.database.currentQuantity * InvestmentsApplication.database.currentPrice <= InvestmentsApplication.database.currentUser.getSumRubles() ||
                InvestmentsApplication.database.currentAction.equals("SELL_BID") &&
                        InvestmentsApplication.database.currentQuantity <= InvestmentsApplication.database.currentUser.getNumberCurrentCompany(InvestmentsApplication.database.currentCompany)) {

            InvestmentsApplication.database.currentPrice = Float.parseFloat(textFieldForPrice.getText());
            if (!new OfferDaoImpl().isExistPrice(InvestmentsApplication.database.currentPrice, InvestmentsApplication.database.currentIdUser)) {
                String query = "INSERT INTO public.offers (id_user, company, action, price, quantity) VALUES (?, ?, ?, ?, ?);";
                try (PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(query)) {
                    preparedStatement.setInt(1, InvestmentsApplication.database.currentIdUser);
                    preparedStatement.setString(2, InvestmentsApplication.database.currentCompany);
                    preparedStatement.setString(3, InvestmentsApplication.database.currentAction);
                    preparedStatement.setFloat(4, InvestmentsApplication.database.currentPrice);
                    preparedStatement.setInt(5, InvestmentsApplication.database.currentQuantity);
                    preparedStatement.execute();
                } catch (SQLException exception) {
                    throw new RuntimeException(exception);
                }
            } else {
                String sql = "UPDATE offers SET quantity=quantity+? WHERE id_user=? AND company=? AND action=? AND price=?";
                try {
                    PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(sql);
                    preparedStatement.setInt(1, InvestmentsApplication.database.currentQuantity);
                    preparedStatement.setInt(2, InvestmentsApplication.database.currentIdUser);
                    preparedStatement.setString(3, InvestmentsApplication.database.currentCompany);
                    preparedStatement.setString(4, InvestmentsApplication.database.currentAction);
                    preparedStatement.setFloat(5, InvestmentsApplication.database.currentPrice);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (InvestmentsApplication.database.currentAction.equals("BUY_BID")) {
                String sql = "UPDATE users SET sumrubles=sumrubles-? WHERE id=?";
                try {
                    PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(sql);
                    preparedStatement.setFloat(1, InvestmentsApplication.database.currentQuantity * InvestmentsApplication.database.currentPrice);
                    preparedStatement.setInt(2, InvestmentsApplication.database.currentIdUser);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (InvestmentsApplication.database.currentAction.equals("SELL_BID")) {
                String sql = "UPDATE users SET " +
                        "sum"+InvestmentsApplication.database.currentCompany.toLowerCase()+"="+
                        "sum"+InvestmentsApplication.database.currentCompany.toLowerCase()
                        + "-" + InvestmentsApplication.database.currentQuantity+
                        " WHERE id=?";
                try {
                    PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(sql);
                    preparedStatement.setInt(1, InvestmentsApplication.database.currentIdUser);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (InvestmentsApplication.database.currentAction.equals("BUY_BID") &&
                InvestmentsApplication.database.currentQuantity * InvestmentsApplication.database.currentPrice > InvestmentsApplication.database.currentUser.getSumRubles()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ОШИБКА :(");
            alert.setHeaderText("не хватает денег для совершения данной операции");
            alert.showAndWait();
        } else if (InvestmentsApplication.database.currentAction.equals("SELL_BID") &&
                InvestmentsApplication.database.currentQuantity > InvestmentsApplication.database.currentUser.getNumberCurrentCompany(InvestmentsApplication.database.currentCompany)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ОШИБКА :(");
            alert.setHeaderText("не хватает акций для совершения данной операции");
            alert.showAndWait();
        } else if (InvestmentsApplication.database.currentAction.equals("BUY")) {
            InvestmentsApplication.database.buyPaper();
        } else if (InvestmentsApplication.database.currentAction.equals("SELL")) {
            InvestmentsApplication.database.sellPaper();
        }


        //после execute переходим на нашу страницу
        Parent wow = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/congratulations.fxml")));
        Scene scene = new Scene(wow);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onClickRbBidBuy(ActionEvent event) {
        InvestmentsApplication.database.currentAction = "BUY_BID";
        textForBid.setVisible(true);
        textFieldForPrice.setVisible(true);
    }

    @FXML
    void onClickRbBidSell(ActionEvent event) {
        InvestmentsApplication.database.currentAction = "SELL_BID";
        textForBid.setVisible(true);
        textFieldForPrice.setVisible(true);
    }

    @FXML
    void onClickRbBuy(ActionEvent event) {
        InvestmentsApplication.database.currentAction = "BUY";
        textForBid.setVisible(false);
        textFieldForPrice.setVisible(false);
    }

    @FXML
    void onClickRbSell(ActionEvent event) {
        InvestmentsApplication.database.currentAction = "SELL";
        textForBid.setVisible(false);
        textFieldForPrice.setVisible(false);
    }

    @FXML
    void onKeyPressedTextFieldForQuantity(KeyEvent event) {
        textFieldForPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                executeButton.setDisable(true);
            } else {
                executeButton.setDisable(false);
            }
        });
        /*if (textFieldForQuantity.getText().equals("") || textFieldForPrice.isVisible() && textFieldForPrice.getText().equals("")) {
            executeButton.setDisable(true);
        }
        else{
            executeButton.setDisable(false);
        }*/
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        try {
            textSumRubles.setText("Баланс - "+InvestmentsApplication.database.currentUser.getSumRubles() + "₽");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            switch (searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).size()){
                case 0:
                    text_1_7.setText("-");
                    text_2_7.setText("-");
                    text_1_8.setText("-");
                    text_2_8.setText("-");
                    text_1_9.setText("-");
                    text_2_9.setText("-");
                    text_1_10.setText("-");
                    text_2_10.setText("-");
                    text_1_11.setText("-");
                    text_2_11.setText("-");
                    text_1_12.setText("-");
                    text_2_12.setText("-");
                    break;
                case 1:
                    text_1_7.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(0)[0])));
                    text_2_7.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(0)[1])));
                    text_1_8.setText("-");
                    text_2_8.setText("-");
                    text_1_9.setText("-");
                    text_2_9.setText("-");
                    text_1_10.setText("-");
                    text_2_10.setText("-");
                    text_1_11.setText("-");
                    text_2_11.setText("-");
                    text_1_12.setText("-");
                    text_2_12.setText("-");
                    break;
                case 2:
                    text_1_7.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(0)[0])));
                    text_2_7.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(0)[1])));
                    text_1_8.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(1)[0])));
                    text_2_8.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(1)[1])));
                    text_1_9.setText("-");
                    text_2_9.setText("-");
                    text_1_10.setText("-");
                    text_2_10.setText("-");
                    text_1_11.setText("-");
                    text_2_11.setText("-");
                    text_1_12.setText("-");
                    text_2_12.setText("-");
                    break;
                case 3:
                    text_1_7.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(0)[0])));
                    text_2_7.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(0)[1])));
                    text_1_8.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(1)[0])));
                    text_2_8.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(1)[1])));
                    text_1_9.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(2)[0])));
                    text_2_9.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(2)[1])));
                    text_1_10.setText("-");
                    text_2_10.setText("-");
                    text_1_11.setText("-");
                    text_2_11.setText("-");
                    text_1_12.setText("-");
                    text_2_12.setText("-");
                    break;
                case 4:
                    text_1_7.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(0)[0])));
                    text_2_7.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(0)[1])));
                    text_1_8.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(1)[0])));
                    text_2_8.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(1)[1])));
                    text_1_9.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(2)[0])));
                    text_2_9.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(2)[1])));
                    text_1_10.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(3)[0])));
                    text_2_10.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(3)[1])));
                    text_1_11.setText("-");
                    text_2_11.setText("-");
                    text_1_12.setText("-");
                    text_2_12.setText("-");
                    break;
                case 5:
                    text_1_7.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(0)[0])));
                    text_2_7.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(0)[1])));
                    text_1_8.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(1)[0])));
                    text_2_8.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(1)[1])));
                    text_1_9.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(2)[0])));
                    text_2_9.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(2)[1])));
                    text_1_10.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(3)[0])));
                    text_2_10.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(3)[1])));
                    text_1_11.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(4)[0])));
                    text_2_11.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(4)[1])));
                    text_1_12.setText("-");
                    text_2_12.setText("-");
                    break;
                case 6:
                    text_1_7.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(0)[0])));
                    text_2_7.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(0)[1])));
                    text_1_8.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(1)[0])));
                    text_2_8.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(1)[1])));
                    text_1_9.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(2)[0])));
                    text_2_9.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(2)[1])));
                    text_1_10.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(3)[0])));
                    text_2_10.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(3)[1])));
                    text_1_11.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(4)[0])));
                    text_2_11.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(4)[1])));
                    text_1_12.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(5)[0])));
                    text_2_12.setText(String.valueOf((searchPriceAndQuantity("BUY_BID", InvestmentsApplication.database.currentCompany).get(5)[1])));
                    break;
            }
            switch (searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).size()) {
                case 0:
                    text_3_6.setText("-");
                    text_2_6.setText("-");
                    text_3_5.setText("-");
                    text_2_5.setText("-");
                    text_3_4.setText("-");
                    text_2_4.setText("-");
                    text_3_3.setText("-");
                    text_2_3.setText("-");
                    text_3_2.setText("-");
                    text_2_2.setText("-");
                    text_3_1.setText("-");
                    text_2_1.setText("-");
                    break;
                case 1:
                    text_3_6.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(0)[0])));
                    text_2_6.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(0)[1])));
                    text_3_5.setText("-");
                    text_2_5.setText("-");
                    text_3_4.setText("-");
                    text_2_4.setText("-");
                    text_3_3.setText("-");
                    text_2_3.setText("-");
                    text_3_2.setText("-");
                    text_2_2.setText("-");
                    text_3_1.setText("-");
                    text_2_1.setText("-");
                    break;
                case 2:
                    text_3_6.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(0)[0])));
                    text_2_6.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(0)[1])));
                    text_3_5.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(1)[0])));
                    text_2_5.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(1)[1])));
                    text_3_4.setText("-");
                    text_2_4.setText("-");
                    text_3_3.setText("-");
                    text_2_3.setText("-");
                    text_3_2.setText("-");
                    text_2_2.setText("-");
                    text_3_1.setText("-");
                    text_2_1.setText("-");
                    break;
                case 3:
                    text_3_6.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(0)[0])));
                    text_2_6.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(0)[1])));
                    text_3_5.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(1)[0])));
                    text_2_5.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(1)[1])));
                    text_3_4.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(2)[0])));
                    text_2_4.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(2)[1])));
                    text_3_3.setText("-");
                    text_2_3.setText("-");
                    text_3_2.setText("-");
                    text_2_2.setText("-");
                    text_3_1.setText("-");
                    text_2_1.setText("-");
                    break;
                case 4:
                    text_3_6.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(0)[0])));
                    text_2_6.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(0)[1])));
                    text_3_5.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(1)[0])));
                    text_2_5.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(1)[1])));
                    text_3_4.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(2)[0])));
                    text_2_4.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(2)[1])));
                    text_3_3.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(3)[0])));
                    text_2_3.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(3)[1])));
                    text_3_2.setText("-");
                    text_2_2.setText("-");
                    text_3_1.setText("-");
                    text_2_1.setText("-");
                    break;
                case 5:
                    text_3_6.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(0)[0])));
                    text_2_6.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(0)[1])));
                    text_3_5.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(1)[0])));
                    text_2_5.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(1)[1])));
                    text_3_4.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(2)[0])));
                    text_2_4.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(2)[1])));
                    text_3_3.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(3)[0])));
                    text_2_3.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(3)[1])));
                    text_3_2.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(4)[0])));
                    text_2_2.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(4)[1])));
                    text_3_1.setText("-");
                    text_2_1.setText("-");
                    break;
                case 6:
                    text_3_6.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(0)[0])));
                    text_2_6.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(0)[1])));
                    text_3_5.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(1)[0])));
                    text_2_5.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(1)[1])));
                    text_3_4.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(2)[0])));
                    text_2_4.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(2)[1])));
                    text_3_3.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(3)[0])));
                    text_2_3.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(3)[1])));
                    text_3_2.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(4)[0])));
                    text_2_2.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(4)[1])));
                    text_3_1.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(5)[0])));
                    text_2_1.setText(String.valueOf((searchPriceAndQuantity("SELL_BID", InvestmentsApplication.database.currentCompany).get(5)[1])));
                    break;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    List<Integer[]> searchPriceAndQuantity(String action, String company) throws SQLException {
        List<Integer[]> list = new ArrayList<>();
        Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
        String sql = "SELECT price, quantity FROM public.offers WHERE offers.company='" + company + "' AND offers.action='"+action+"';";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet != null) {
            while (resultSet.next()) {
                list.add(new Integer[]{
                    resultSet.getInt("quantity"),
                    resultSet.getInt("price")
                }
                );
            }
        }


        Collections.sort(list, Comparator.comparingInt(arr -> arr[1]));
        if(action.equals("BUY_BID")) Collections.reverse(list);
        return list;
    }
}

