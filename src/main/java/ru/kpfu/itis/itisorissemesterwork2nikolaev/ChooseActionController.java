package ru.kpfu.itis.itisorissemesterwork2nikolaev;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.tcp.Client;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class ChooseActionController implements Initializable {
    List list;
    @FXML
    private Button buttonExit;
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
    private Text text_1_10;
    @FXML
    private Text text_1_11;
    @FXML
    private Text text_1_12;
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
    private Text text_3_2;
    @FXML
    private Text text_3_3;
    @FXML
    private Text text_3_4;
    @FXML
    private Text text_3_5;
    @FXML
    private Text text_3_6;
    private Client client = InvestmentsApplication.getClient();

    @FXML
    void onClickBack(ActionEvent event) throws IOException {
        loadNewScene(event, "profile");
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

    public void setClient(Client client) {
        this.client = client;
    }
    void loadNewScene(ActionEvent event, String sceneName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/" + sceneName + ".fxml"));
        Parent root = loader.load(); // Загрузка FXML и получение корневого элемента
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void onClickExecute(ActionEvent actionEvent) {
        try {
            String[] args = client.execute(InvestmentsApplication.database.currentAction,
                    InvestmentsApplication.database.currentCompany,
                    InvestmentsApplication.database.currentUser.getId(),
                    textFieldForPrice.getText().equals("") ? 0 : Float.parseFloat(textFieldForPrice.getText()),
                    Integer.parseInt(textFieldForQuantity.getText()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(args[0]);
            alert.setHeaderText(args[1]);
            alert.showAndWait();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private Timeline timeline;

    @FXML
    private ToggleGroup action;

    // Другие элементы управления

    void updateFields(){
        list = InvestmentsApplication.getClient().sendCompany(InvestmentsApplication.database.currentCompany);
        List<Integer[]> listBuy = (List<Integer[]>) list.get(0);
        List<Integer[]> listSell = (List<Integer[]>) list.get(1);
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

        switch (listBuy.size()) {
            case 0:
                break;
            case 1:
                text_1_7.setText(String.valueOf(listBuy.get(0)[0]));
                text_2_7.setText(String.valueOf(listBuy.get(0)[1]));
                break;
            case 2:
                text_1_7.setText(String.valueOf(listBuy.get(0)[0]));
                text_2_7.setText(String.valueOf(listBuy.get(0)[1]));
                text_1_8.setText(String.valueOf(listBuy.get(1)[0]));
                text_2_8.setText(String.valueOf(listBuy.get(1)[1]));
                break;
            case 3:
                text_1_7.setText(String.valueOf(listBuy.get(0)[0]));
                text_2_7.setText(String.valueOf(listBuy.get(0)[1]));
                text_1_8.setText(String.valueOf(listBuy.get(1)[0]));
                text_2_8.setText(String.valueOf(listBuy.get(1)[1]));
                text_1_9.setText(String.valueOf(listBuy.get(2)[0]));
                text_2_9.setText(String.valueOf(listBuy.get(2)[1]));
                break;
            case 4:
                text_1_7.setText(String.valueOf(listBuy.get(0)[0]));
                text_2_7.setText(String.valueOf(listBuy.get(0)[1]));
                text_1_8.setText(String.valueOf(listBuy.get(1)[0]));
                text_2_8.setText(String.valueOf(listBuy.get(1)[1]));
                text_1_9.setText(String.valueOf(listBuy.get(2)[0]));
                text_2_9.setText(String.valueOf(listBuy.get(2)[1]));
                text_1_10.setText(String.valueOf(listBuy.get(3)[0]));
                text_2_10.setText(String.valueOf(listBuy.get(3)[1]));
                break;
            case 5:
                text_1_7.setText(String.valueOf(listBuy.get(0)[0]));
                text_2_7.setText(String.valueOf(listBuy.get(0)[1]));
                text_1_8.setText(String.valueOf(listBuy.get(1)[0]));
                text_2_8.setText(String.valueOf(listBuy.get(1)[1]));
                text_1_9.setText(String.valueOf(listBuy.get(2)[0]));
                text_2_9.setText(String.valueOf(listBuy.get(2)[1]));
                text_1_10.setText(String.valueOf(listBuy.get(3)[0]));
                text_2_10.setText(String.valueOf(listBuy.get(3)[1]));
                text_1_11.setText(String.valueOf(listBuy.get(4)[0]));
                text_2_11.setText(String.valueOf(listBuy.get(4)[1]));
                break;
            case 6:
                text_1_7.setText(String.valueOf(listBuy.get(0)[0]));
                text_2_7.setText(String.valueOf(listBuy.get(0)[1]));
                text_1_8.setText(String.valueOf(listBuy.get(1)[0]));
                text_2_8.setText(String.valueOf(listBuy.get(1)[1]));
                text_1_9.setText(String.valueOf(listBuy.get(2)[0]));
                text_2_9.setText(String.valueOf(listBuy.get(2)[1]));
                text_1_10.setText(String.valueOf(listBuy.get(3)[0]));
                text_2_10.setText(String.valueOf(listBuy.get(3)[1]));
                text_1_11.setText(String.valueOf(listBuy.get(4)[0]));
                text_2_11.setText(String.valueOf(listBuy.get(4)[1]));
                text_1_12.setText(String.valueOf(listBuy.get(5)[0]));
                text_2_12.setText(String.valueOf(listBuy.get(5)[1]));
                break;
        }
        switch (listSell.size()) {
            case 0:
                break;
            case 1:
                text_3_6.setText(String.valueOf(listSell.get(0)[0]));
                text_2_6.setText(String.valueOf(listSell.get(0)[1]));
                break;
            case 2:
                text_3_6.setText(String.valueOf(listSell.get(0)[0]));
                text_2_6.setText(String.valueOf(listSell.get(0)[1]));
                text_3_5.setText(String.valueOf(listSell.get(1)[0]));
                text_2_5.setText(String.valueOf(listSell.get(1)[1]));
                break;
            case 3:
                text_3_6.setText(String.valueOf(listSell.get(0)[0]));
                text_2_6.setText(String.valueOf(listSell.get(0)[1]));
                text_3_5.setText(String.valueOf(listSell.get(1)[0]));
                text_2_5.setText(String.valueOf(listSell.get(1)[1]));
                text_3_4.setText(String.valueOf(listSell.get(2)[0]));
                text_2_4.setText(String.valueOf(listSell.get(2)[1]));
                break;
            case 4:
                text_3_6.setText(String.valueOf(listSell.get(0)[0]));
                text_2_6.setText(String.valueOf(listSell.get(0)[1]));
                text_3_5.setText(String.valueOf(listSell.get(1)[0]));
                text_2_5.setText(String.valueOf(listSell.get(1)[1]));
                text_3_4.setText(String.valueOf(listSell.get(2)[0]));
                text_2_4.setText(String.valueOf(listSell.get(2)[1]));
                text_3_3.setText(String.valueOf(listSell.get(3)[0]));
                text_2_3.setText(String.valueOf(listSell.get(3)[1]));
                break;
            case 5:
                text_3_6.setText(String.valueOf(listSell.get(0)[0]));
                text_2_6.setText(String.valueOf(listSell.get(0)[1]));
                text_3_5.setText(String.valueOf(listSell.get(1)[0]));
                text_2_5.setText(String.valueOf(listSell.get(1)[1]));
                text_3_4.setText(String.valueOf(listSell.get(2)[0]));
                text_2_4.setText(String.valueOf(listSell.get(2)[1]));
                text_3_3.setText(String.valueOf(listSell.get(3)[0]));
                text_2_3.setText(String.valueOf(listSell.get(3)[1]));
                text_3_2.setText(String.valueOf(listSell.get(4)[0]));
                text_2_2.setText(String.valueOf(listSell.get(4)[1]));
                break;
            case 6:
                text_3_6.setText(String.valueOf(listSell.get(0)[0]));
                text_2_6.setText(String.valueOf(listSell.get(0)[1]));
                text_3_5.setText(String.valueOf(listSell.get(1)[0]));
                text_2_5.setText(String.valueOf(listSell.get(1)[1]));
                text_3_4.setText(String.valueOf(listSell.get(2)[0]));
                text_2_4.setText(String.valueOf(listSell.get(2)[1]));
                text_3_3.setText(String.valueOf(listSell.get(3)[0]));
                text_2_3.setText(String.valueOf(listSell.get(3)[1]));
                text_3_2.setText(String.valueOf(listSell.get(4)[0]));
                text_2_2.setText(String.valueOf(listSell.get(4)[1]));
                text_3_1.setText(String.valueOf(listSell.get(5)[0]));
                text_2_1.setText(String.valueOf(listSell.get(5)[1]));
                break;
        }
    }
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        buttonExit.setOnAction(event->{
            try {
                onClickBack(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        rb_buy.setOnAction(event->{
            onClickRbBuy(event);
        });
        rb_sell.setOnAction(event->{
            onClickRbSell(event);
        });
        rb_bid_buy.setOnAction(event->{
            onClickRbBidBuy(event);
        });
        rb_bid_sell.setOnAction(event->{
            onClickRbBidSell(event);
        });
        executeButton.setOnAction(event -> {
            onClickExecute(event);
        });
        list = InvestmentsApplication.getClient().sendCompany(InvestmentsApplication.database.currentCompany);
        List<Integer[]> listBuy = (List<Integer[]>) list.get(0);
        List<Integer[]> listSell = (List<Integer[]>) list.get(1);
        try {
            textSumRubles.setText("Баланс - " + client.getSumRubles(InvestmentsApplication.database.currentUser.getName()) + "₽");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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

        switch (listBuy.size()) {
            case 0:
                break;
            case 1:
                text_1_7.setText(String.valueOf(listBuy.get(0)[0]));
                text_2_7.setText(String.valueOf(listBuy.get(0)[1]));
                break;
            case 2:
                text_1_7.setText(String.valueOf(listBuy.get(0)[0]));
                text_2_7.setText(String.valueOf(listBuy.get(0)[1]));
                text_1_8.setText(String.valueOf(listBuy.get(1)[0]));
                text_2_8.setText(String.valueOf(listBuy.get(1)[1]));
                break;
            case 3:
                text_1_7.setText(String.valueOf(listBuy.get(0)[0]));
                text_2_7.setText(String.valueOf(listBuy.get(0)[1]));
                text_1_8.setText(String.valueOf(listBuy.get(1)[0]));
                text_2_8.setText(String.valueOf(listBuy.get(1)[1]));
                text_1_9.setText(String.valueOf(listBuy.get(2)[0]));
                text_2_9.setText(String.valueOf(listBuy.get(2)[1]));
                break;
            case 4:
                text_1_7.setText(String.valueOf(listBuy.get(0)[0]));
                text_2_7.setText(String.valueOf(listBuy.get(0)[1]));
                text_1_8.setText(String.valueOf(listBuy.get(1)[0]));
                text_2_8.setText(String.valueOf(listBuy.get(1)[1]));
                text_1_9.setText(String.valueOf(listBuy.get(2)[0]));
                text_2_9.setText(String.valueOf(listBuy.get(2)[1]));
                text_1_10.setText(String.valueOf(listBuy.get(3)[0]));
                text_2_10.setText(String.valueOf(listBuy.get(3)[1]));
                break;
            case 5:
                text_1_7.setText(String.valueOf(listBuy.get(0)[0]));
                text_2_7.setText(String.valueOf(listBuy.get(0)[1]));
                text_1_8.setText(String.valueOf(listBuy.get(1)[0]));
                text_2_8.setText(String.valueOf(listBuy.get(1)[1]));
                text_1_9.setText(String.valueOf(listBuy.get(2)[0]));
                text_2_9.setText(String.valueOf(listBuy.get(2)[1]));
                text_1_10.setText(String.valueOf(listBuy.get(3)[0]));
                text_2_10.setText(String.valueOf(listBuy.get(3)[1]));
                text_1_11.setText(String.valueOf(listBuy.get(4)[0]));
                text_2_11.setText(String.valueOf(listBuy.get(4)[1]));
                break;
            case 6:
                text_1_7.setText(String.valueOf(listBuy.get(0)[0]));
                text_2_7.setText(String.valueOf(listBuy.get(0)[1]));
                text_1_8.setText(String.valueOf(listBuy.get(1)[0]));
                text_2_8.setText(String.valueOf(listBuy.get(1)[1]));
                text_1_9.setText(String.valueOf(listBuy.get(2)[0]));
                text_2_9.setText(String.valueOf(listBuy.get(2)[1]));
                text_1_10.setText(String.valueOf(listBuy.get(3)[0]));
                text_2_10.setText(String.valueOf(listBuy.get(3)[1]));
                text_1_11.setText(String.valueOf(listBuy.get(4)[0]));
                text_2_11.setText(String.valueOf(listBuy.get(4)[1]));
                text_1_12.setText(String.valueOf(listBuy.get(5)[0]));
                text_2_12.setText(String.valueOf(listBuy.get(5)[1]));
                break;
        }
        switch (listSell.size()) {
            case 0:
                break;
            case 1:
                text_3_6.setText(String.valueOf(listSell.get(0)[0]));
                text_2_6.setText(String.valueOf(listSell.get(0)[1]));
                break;
            case 2:
                text_3_6.setText(String.valueOf(listSell.get(0)[0]));
                text_2_6.setText(String.valueOf(listSell.get(0)[1]));
                text_3_5.setText(String.valueOf(listSell.get(1)[0]));
                text_2_5.setText(String.valueOf(listSell.get(1)[1]));
                break;
            case 3:
                text_3_6.setText(String.valueOf(listSell.get(0)[0]));
                text_2_6.setText(String.valueOf(listSell.get(0)[1]));
                text_3_5.setText(String.valueOf(listSell.get(1)[0]));
                text_2_5.setText(String.valueOf(listSell.get(1)[1]));
                text_3_4.setText(String.valueOf(listSell.get(2)[0]));
                text_2_4.setText(String.valueOf(listSell.get(2)[1]));
                break;
            case 4:
                text_3_6.setText(String.valueOf(listSell.get(0)[0]));
                text_2_6.setText(String.valueOf(listSell.get(0)[1]));
                text_3_5.setText(String.valueOf(listSell.get(1)[0]));
                text_2_5.setText(String.valueOf(listSell.get(1)[1]));
                text_3_4.setText(String.valueOf(listSell.get(2)[0]));
                text_2_4.setText(String.valueOf(listSell.get(2)[1]));
                text_3_3.setText(String.valueOf(listSell.get(3)[0]));
                text_2_3.setText(String.valueOf(listSell.get(3)[1]));
                break;
            case 5:
                text_3_6.setText(String.valueOf(listSell.get(0)[0]));
                text_2_6.setText(String.valueOf(listSell.get(0)[1]));
                text_3_5.setText(String.valueOf(listSell.get(1)[0]));
                text_2_5.setText(String.valueOf(listSell.get(1)[1]));
                text_3_4.setText(String.valueOf(listSell.get(2)[0]));
                text_2_4.setText(String.valueOf(listSell.get(2)[1]));
                text_3_3.setText(String.valueOf(listSell.get(3)[0]));
                text_2_3.setText(String.valueOf(listSell.get(3)[1]));
                text_3_2.setText(String.valueOf(listSell.get(4)[0]));
                text_2_2.setText(String.valueOf(listSell.get(4)[1]));
                break;
            case 6:
                text_3_6.setText(String.valueOf(listSell.get(0)[0]));
                text_2_6.setText(String.valueOf(listSell.get(0)[1]));
                text_3_5.setText(String.valueOf(listSell.get(1)[0]));
                text_2_5.setText(String.valueOf(listSell.get(1)[1]));
                text_3_4.setText(String.valueOf(listSell.get(2)[0]));
                text_2_4.setText(String.valueOf(listSell.get(2)[1]));
                text_3_3.setText(String.valueOf(listSell.get(3)[0]));
                text_2_3.setText(String.valueOf(listSell.get(3)[1]));
                text_3_2.setText(String.valueOf(listSell.get(4)[0]));
                text_2_2.setText(String.valueOf(listSell.get(4)[1]));
                text_3_1.setText(String.valueOf(listSell.get(5)[0]));
                text_2_1.setText(String.valueOf(listSell.get(5)[1]));
                break;
        }
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            updateFields(); // Вызываем метод обновления текстовых полей каждую секунду
        }));
        timeline.setCycleCount(Animation.INDEFINITE); // Устанавливаем бесконечное повторение
        timeline.play();
    }}

