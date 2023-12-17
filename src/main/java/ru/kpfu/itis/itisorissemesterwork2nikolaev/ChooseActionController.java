package ru.kpfu.itis.itisorissemesterwork2nikolaev;

import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.dao.impl.OfferDaoImpl;

import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;


public class ChooseActionController implements Initializable {


    @FXML
    private ToggleGroup action;

    @FXML
    private Button buttonExit;

    @FXML
    public static Label errorLabel;

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
    private Text text_1_1;



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



        if (InvestmentsApplication.database.currentAction.equals("BUY_BID") || InvestmentsApplication.database.currentAction.equals("SELL_BID")) {
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
                    InvestmentsApplication.database.currentPrice = -1;
                    InvestmentsApplication.database.currentQuantity = -1;
                } catch (SQLException exception) {
                    throw new RuntimeException(exception);
                }
            } else {
                String sql = "UPDATE offers SET quantity=quantity+? WHERE id_user=? AND name=? AND action=? AND price=?";
                try {
                    PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(sql);
                    preparedStatement.setInt(1, InvestmentsApplication.database.currentIdUser);
                    preparedStatement.setInt(2, InvestmentsApplication.database.currentQuantity);
                    preparedStatement.setString(3, InvestmentsApplication.database.currentCompany);
                    preparedStatement.setString(4, InvestmentsApplication.database.currentAction);
                    preparedStatement.setFloat(5, InvestmentsApplication.database.currentPrice);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            if (InvestmentsApplication.database.currentAction.equals("BUY")){
                InvestmentsApplication.database.buyPaper();
            }
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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        text_1_1.setText(String.valueOf(Math.random()));
        //onKeyPressedTextFieldForQuantity(new KeyEvent(new Object()));
    }
}

