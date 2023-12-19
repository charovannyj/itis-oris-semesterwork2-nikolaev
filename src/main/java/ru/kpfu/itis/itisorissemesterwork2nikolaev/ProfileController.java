package ru.kpfu.itis.itisorissemesterwork2nikolaev;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.tcp.Client;

import java.io.IOException;
import java.sql.SQLException;

public class ProfileController {
    @FXML
    private Text textBalance;
    @FXML
    private Text numberAeroflot;

    @FXML
    private Text numberGold;

    @FXML
    private Text numberRosneft;

    @FXML
    private Text numberSber;

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
    private AnchorPane paneAeroflot;
    @FXML
    private AnchorPane paneGold;
    @FXML
    private AnchorPane paneRosneft;
    @FXML
    private AnchorPane paneSber;
    private Client client = InvestmentsApplication.getClient();

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    void onClickBack(MouseEvent event) throws IOException {
        loadNewScene(event, "main");
    }

    void loadNewScene(MouseEvent event, String sceneName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/" + sceneName + ".fxml"));
        Parent root = loader.load(); // Загрузка FXML и получение корневого элемента
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onClickSber(MouseEvent event) throws IOException {
        InvestmentsApplication.database.currentCompany = Company.SBER.value;
        loadNewScene(event, "chooseAction");
    }

    @FXML
    void onClickRosneft(MouseEvent event) throws IOException {
        InvestmentsApplication.database.currentCompany = Company.ROSNEFT.value;
        loadNewScene(event, "chooseAction");
    }

    @FXML
    void onClickAeroflot(MouseEvent event) throws IOException {
        InvestmentsApplication.database.currentCompany = Company.AEROFLOT.value;
        loadNewScene(event, "chooseAction");
    }

    @FXML
    void onClickGold(MouseEvent event) throws IOException {
        InvestmentsApplication.database.currentCompany = Company.GOLD.value;
        loadNewScene(event, "chooseAction");
    }

    @FXML
    void initialize() throws SQLException, IOException, ClassNotFoundException {
        numberAeroflot.setText((client.getNumberOfCompany("AEROFLOT", InvestmentsApplication.database.currentUser.getId()))+" шт.");
        numberGold.setText((client.getNumberOfCompany("GOLD", InvestmentsApplication.database.currentUser.getId()))+" шт.");
        numberRosneft.setText((client.getNumberOfCompany("ROSNEFT", InvestmentsApplication.database.currentUser.getId()))+" шт.");
        numberSber.setText((client.getNumberOfCompany("SBER", InvestmentsApplication.database.currentUser.getId()))+" шт.");
        textBalance.setText(String.valueOf(client.getSumRubles(InvestmentsApplication.database.currentUser.name))+"₽");
        textAeroflotSell.setText(String.valueOf(client.searchPrice("SELL_BID", "AEROFLOT")));
        textAeroflotBuy.setText(String.valueOf(client.searchPrice("BUY_BID", "AEROFLOT")));
        textRosneftSell.setText(String.valueOf(client.searchPrice("SELL_BID", "ROSNEFT")));
        textRosneftBuy.setText(String.valueOf(client.searchPrice("BUY_BID", "ROSNEFT")));
        textSberSell.setText(String.valueOf(client.searchPrice("SELL_BID", "SBER")));
        textSberBuy.setText(String.valueOf(client.searchPrice("BUY_BID", "SBER")));
        textGoldSell.setText(String.valueOf(client.searchPrice("SELL_BID", "GOLD")));
        textGoldBuy.setText(String.valueOf(client.searchPrice("BUY_BID", "GOLD")));

        paneSber.setOnMouseClicked(e -> {
            try {
                onClickSber(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        paneRosneft.setOnMouseClicked(e -> {
            try {
                onClickRosneft(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        paneAeroflot.setOnMouseClicked(e -> {
            try {
                onClickAeroflot(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        paneGold.setOnMouseClicked(e -> {
            try {
                onClickGold(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
