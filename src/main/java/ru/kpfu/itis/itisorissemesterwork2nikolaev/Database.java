package ru.kpfu.itis.itisorissemesterwork2nikolaev;

import ru.kpfu.itis.itisorissemesterwork2nikolaev.model.Offer;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Database {
    public int currentIdUser;
    public User currentUser;

    public String currentName;

    public float currentPrice;
    public int currentQuantity;
    public String currentCompany;
    public String currentAction;

    static HashMap<Integer, Integer> buySber = new HashMap<>();
    HashMap<Integer, Integer> sellSber = new HashMap<>();
    HashMap<Integer, Integer> buyRosneft = new HashMap<>();
    HashMap<Integer, Integer> sellRosneft = new HashMap<>();
    HashMap<Integer, Integer> buyAeroflot = new HashMap<>();
    HashMap<Integer, Integer> sellAeroflot = new HashMap<>();
    HashMap<Integer, Integer> buyGold = new HashMap<>();
    HashMap<Integer, Integer> sellGold = new HashMap<>();
    static List<Integer[]> prices = new ArrayList<>();

    static void buyPaper() {
        try {
            //формируем массив всех заявок по выбранному 1 из 2 условий
            Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
            String sql = "SELECT id_user, price, quantity FROM public.offers WHERE offers.company='" + InvestmentsApplication.database.currentCompany + "' AND offers.action='SELL_BID';";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    for (int i = 0; i < resultSet.getInt("quantity"); i++) {
                        prices.add(new Integer[]{
                                        resultSet.getInt("id_user"),
                                        resultSet.getInt("price")

                                    }
                        );
                    }
                }
            }
            Collections.sort(prices, Comparator.comparingInt(arr -> arr[1]));
            int sum = 0;
            int count = InvestmentsApplication.database.currentQuantity;
            int i = 0;
            for (Integer[] price : prices) {
                if (i<count){
                    sum += price[1];
                    i++;
                }
                else{
                    break;
                }
            }
            int size = prices.size();
            //рассмотрим 4 случая хватает ли денег и заявок или нет
            //1 случай - хватает и того и того
            //2 - деньги есть, заявок недостаточно
            //3 - денег нет
            if(InvestmentsApplication.database.currentUser.getSumRubles()>=sum && InvestmentsApplication.database.currentQuantity<=size){
                for (int r = 0; r < count; r++){
                    changeMoneyOnPaper(prices.get(r)[0], prices.get(r)[1], "SELL_BID");
                }
            } else if (InvestmentsApplication.database.currentUser.getSumRubles()>=sum && InvestmentsApplication.database.currentQuantity>size){
                // покупаем все возможные заявки и говорим пользователю что купили все возможные и часть денег обратно и оповещаем
                for (int k = 0; k < count; k++){
                    changeMoneyOnPaper(prices.get(k)[0], prices.get(k)[1], "SELL_BID");
                }
                //накинуть оповещение
            } else if(InvestmentsApplication.database.currentUser.getSumRubles()<sum){
                //тут npe вылезает...............................
                //ChooseActionController.setErrorLabelVisible(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    static void changeMoneyOnPaper(int id_user, float price, String action){
        //апдейтим офферы подчищая купленные
        String sql1 = "UPDATE offers \n" +
                "SET quantity = CASE \n" +
                "    WHEN quantity > 0 THEN quantity - 1 \n" +
                "    ELSE 0 \n" +
                "END \n" +
                "WHERE id_user = ? AND company = ? AND action = ? AND price = ?;\n" +
                "\n" +
                "DELETE FROM offers \n" +
                "WHERE id_user = ? AND company = ? AND action = ? AND price = ? AND quantity = 0;";
        try {
            PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(sql1);
            preparedStatement.setInt(1, id_user);
            preparedStatement.setString(2, InvestmentsApplication.database.currentCompany);
            preparedStatement.setString(3, action);
            preparedStatement.setFloat(4, price);
            preparedStatement.setInt(5, id_user);
            preparedStatement.setString(6, InvestmentsApplication.database.currentCompany);
            preparedStatement.setString(7, action);
            preparedStatement.setFloat(8, price);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //пополняем баланс и убираем акции у продавца(мы покупатели) в users
        String sql2 = null;
        switch (InvestmentsApplication.database.currentCompany){
            case "SBER":
                sql2 = "UPDATE users SET sumrubles=sumrubles+?, sumsber=sumsber-1 WHERE id=?";
                break;
            case "ROSNEFT":
                sql2 = "UPDATE users SET sumrubles=sumrubles+?, sumrosneft=sumrosneft-1 WHERE id=?";
                break;
            case "AEROFLOT":
                sql2 = "UPDATE users SET sumrubles=sumrubles+?, sumaeroflot=sumaeroflot-1 WHERE id=?";
                break;
            case "GOLD":
                sql2 = "UPDATE users SET sumrubles=sumrubles+?, sumgold=sumgold-1 WHERE id=?";
                break;
        }
        try {
            PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(sql2);
            preparedStatement.setFloat(1, price);
            preparedStatement.setInt(2, id_user);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //теперь нам понижаем баланс и повышаем кол-во акций
        String sql3 = null;
        switch (InvestmentsApplication.database.currentCompany){
            case "SBER":
                sql3 = "UPDATE users SET sumrubles=sumrubles-?, sumsber=sumsber+1 WHERE id=?";
                break;
            case "ROSNEFT":
                sql3 = "UPDATE users SET sumrubles=sumrubles-?, sumrosneft=sumrosneft+1 WHERE id=?";
                break;
            case "AEROFLOT":
                sql3 = "UPDATE users SET sumrubles=sumrubles-?, sumaeroflot=sumaeroflot+1 WHERE id=?";
                break;
            case "GOLD":
                sql3 = "UPDATE users SET sumrubles=sumrubles-?, sumgold=sumgold+1 WHERE id=?";
                break;
        }
        try {
            PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(sql3);
            preparedStatement.setFloat(1, price);
            preparedStatement.setInt(2, InvestmentsApplication.database.currentIdUser);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    static void sellPaper() {
        try {
            //формируем массив всех заявок по выбранному 1 из 2 условий
            Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
            String sql = "SELECT id_user, price, quantity FROM public.offers WHERE offers.company='" + InvestmentsApplication.database.currentCompany + "' AND offers.action='BUY_BID';";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    for (int i = 0; i < resultSet.getInt("quantity"); i++) {
                        prices.add(new Integer[]{
                                        resultSet.getInt("id_user"),
                                        resultSet.getInt("price")

                                }
                        );
                    }
                }
            }
            Collections.sort(prices, Comparator.comparingInt(arr -> arr[1]));
            Collections.reverse(prices);
            int sum = 0;
            int count = InvestmentsApplication.database.currentQuantity;
            int i = 0;
            for (Integer[] price : prices) {
                if (i<count){
                    sum += price[1];
                    i++;
                }
                else{
                    break;
                }
            }
            int size = prices.size();
            //рассмотрим 4 случая хватает ли акций и заявок или нет
            //1 случай - хватает и того и того
            //2 - акции есть, заявок недостаточно
            //3 - акций нет
            if(InvestmentsApplication.database.currentUser.getNumberCurrentCompany(InvestmentsApplication.database.currentCompany)>=InvestmentsApplication.database.currentQuantity &&
                    InvestmentsApplication.database.currentQuantity<=size){
                for (int r = 0; r < count; r++){
                    changePaperOnMoney(prices.get(r)[0], prices.get(r)[1], "BUY_BID");
                }
            } else if (InvestmentsApplication.database.currentUser.getNumberCurrentCompany(InvestmentsApplication.database.currentCompany)>=InvestmentsApplication.database.currentQuantity &&
                    InvestmentsApplication.database.currentQuantity>size){
                // продаем все возможные заявки и говорим пользователю что продали все возможные и часть денег обратно и оповещаем
                for (int k = 0; k < count; k++){
                    changePaperOnMoney(prices.get(k)[0], prices.get(k)[1], "BUY_BID");
                }
                //накинуть оповещение
            } else {
                //недостаточно акций для выставления такой заявки
                //ChooseActionController.setErrorLabelVisible(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    static void changePaperOnMoney(int id_user, float price, String action){
        //апдейтим офферы подчищая купленные
        String sql1 = "UPDATE offers \n" +
                "SET quantity = CASE \n" +
                "    WHEN quantity > 0 THEN quantity - 1 \n" +
                "    ELSE 0 \n" +
                "END \n" +
                "WHERE id_user = ? AND company = ? AND action = ? AND price = ?;\n" +
                "\n" +
                "DELETE FROM offers \n" +
                "WHERE id_user = ? AND company = ? AND action = ? AND price = ? AND quantity = 0;";
        try {
            PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(sql1);
            preparedStatement.setInt(1, id_user);
            preparedStatement.setString(2, InvestmentsApplication.database.currentCompany);
            preparedStatement.setString(3, action);
            preparedStatement.setFloat(4, price);
            preparedStatement.setInt(5, id_user);
            preparedStatement.setString(6, InvestmentsApplication.database.currentCompany);
            preparedStatement.setString(7, action);
            preparedStatement.setFloat(8, price);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //пополняем баланс и убираем акции у продавца(мы покупатели) в users
        String sql2 = null;
        switch (InvestmentsApplication.database.currentCompany){
            case "SBER":
                sql2 = "UPDATE users SET sumrubles=sumrubles-?, sumsber=sumsber+1 WHERE id=?";
                break;
            case "ROSNEFT":
                sql2 = "UPDATE users SET sumrubles=sumrubles-?, sumrosneft=sumrosneft+1 WHERE id=?";
                break;
            case "AEROFLOT":
                sql2 = "UPDATE users SET sumrubles=sumrubles-?, sumaeroflot=sumaeroflot+1 WHERE id=?";
                break;
            case "GOLD":
                sql2 = "UPDATE users SET sumrubles=sumrubles-?, sumgold=sumgold+1 WHERE id=?";
                break;
        }
        try {
            PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(sql2);
            preparedStatement.setFloat(1, price);
            preparedStatement.setInt(2, id_user);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //теперь нам понижаем баланс и повышаем кол-во акций
        String sql3 = null;
        switch (InvestmentsApplication.database.currentCompany){
            case "SBER":
                sql3 = "UPDATE users SET sumrubles=sumrubles+?, sumsber=sumsber-1 WHERE id=?";
                break;
            case "ROSNEFT":
                sql3 = "UPDATE users SET sumrubles=sumrubles+?, sumrosneft=sumrosneft-1 WHERE id=?";
                break;
            case "AEROFLOT":
                sql3 = "UPDATE users SET sumrubles=sumrubles+?, sumaeroflot=sumaeroflot-1 WHERE id=?";
                break;
            case "GOLD":
                sql3 = "UPDATE users SET sumrubles=sumrubles+?, sumgold=sumgold-1 WHERE id=?";
                break;
        }
        try {
            PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(sql3);
            preparedStatement.setFloat(1, price);
            preparedStatement.setInt(2, InvestmentsApplication.database.currentIdUser);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

enum Company {
    SBER("SBER"),
    ROSNEFT("ROSNEFT"),
    AEROFLOT("AEROFLOT"),
    GOLD("GOLD");

    public String value;

    Company(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
