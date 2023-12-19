package ru.kpfu.itis.itisorissemesterwork2nikolaev.tcp;

import javafx.fxml.FXML;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.DatabaseConnectionUtil;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.InvestmentsApplication;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.dao.impl.OfferDaoImpl;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static ru.kpfu.itis.itisorissemesterwork2nikolaev.InvestmentsController.isExistUser;

public class Server {
    private ServerSocket serverSocket;
    private Connection databaseConnection;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            databaseConnection = DatabaseConnectionUtil.getConnection(); // получаем соединение с базой данных
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Server started and listening for connections...");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);

                ClientHandler clientHandler = new ClientHandler(socket);
                new Thread(clientHandler).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private ObjectInputStream input;
        private ObjectOutputStream output;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                input = new ObjectInputStream(clientSocket.getInputStream());
                output = new ObjectOutputStream(clientSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {

                    String request = (String) input.readObject();

                    if (request.startsWith("LOGIN")) {
                        String username = request.substring(6);
                        signInOrUpUser(username);
                        output.writeObject(new User(username));
                    } else if (request.startsWith("COMPANY")) {
                        String company = request.substring(8);
                        List<Integer[]> listBuy = searchPriceAndQuantity("BUY_BID", company);
                        output.writeObject(listBuy);
                        List<Integer[]> listSell = searchPriceAndQuantity("SELL_BID", company);
                        output.writeObject(listSell);
                    } else if (request.startsWith("BUYSELLPRICE")) {
                        String[] args = request.split(" ");
                        float price = searchPrice(args[2], args[1]);
                        output.writeObject(price);
                    } else if (request.startsWith("EXECUTE")) {
                        String[] args = request.split(" ");
                        String action = args[1];
                        String company = args[2];
                        int id_user = Integer.parseInt(args[3]);
                        Float price = Float.parseFloat(args[4]);
                        int quantity = Integer.parseInt(args[5]);
                        String[] ans = onClickExecute(action, company, id_user, price, quantity);
                        output.writeObject(ans);
                    } else if (request.startsWith("GETSUMRUBLES")) {
                        String name = request.substring(13);
                        float sum = getSumRubles(name);
                        output.writeObject(sum);
                    }
                }
            } catch (IOException | ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        public static int getId(String name) throws SQLException {
            int id = 0;
            Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
            String sql = "SELECT * FROM public.users WHERE users.name='" + name + "';";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    id = resultSet.getInt("id");
                }
            }
            return id;
        }
        public int getSumRubles(String name) throws SQLException {
            int sumRubles = 0;
            Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
            String sql = "SELECT * FROM public.users WHERE users.name='" + name + "';";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    sumRubles = resultSet.getInt("sumrubles");
                }
            }
            return sumRubles;
        }

        public int getSumRubles(int id) throws SQLException {
            int sumRubles = 0;
            Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
            String sql = "SELECT * FROM public.users WHERE users.id='" + id + "';";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    sumRubles = resultSet.getInt("sumrubles");
                }
            }
            return sumRubles;
        }

        public int getNumberCurrentCompany(String company, String name) throws SQLException {
            int sumSber = 0;
            int sumRosneft = 0;
            int sumAeroflot = 0;
            int sumGold = 0;
            Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
            switch (company) {
                case ("SBER"):
                    String sql1 = "SELECT sumsber FROM public.users WHERE users.name='" + name + "';";
                    ResultSet resultSet1 = statement.executeQuery(sql1);
                    if (resultSet1 != null) {
                        while (resultSet1.next()) {
                            sumSber = resultSet1.getInt("sumsber");
                        }
                    }
                    return sumSber;
                case ("ROSNEFT"):
                    String sql2 = "SELECT sumrosneft FROM public.users WHERE users.name='" + name + "';";
                    ResultSet resultSet2 = statement.executeQuery(sql2);
                    if (resultSet2 != null) {
                        while (resultSet2.next()) {
                            sumRosneft = resultSet2.getInt("sumrosneft");
                        }
                    }
                    return sumRosneft;
                case ("AEROFLOT"):
                    String sql3 = "SELECT sumaeroflot FROM public.users WHERE users.name='" + name + "';";
                    ResultSet resultSet3 = statement.executeQuery(sql3);
                    if (resultSet3 != null) {
                        while (resultSet3.next()) {
                            sumAeroflot = resultSet3.getInt("sumaeroflot");
                        }
                    }
                    return sumAeroflot;
                case ("GOLD"):
                    String sql4 = "SELECT sumgold FROM public.users WHERE users.name='" + name + "';";
                    ResultSet resultSet4 = statement.executeQuery(sql4);
                    if (resultSet4 != null) {
                        while (resultSet4.next()) {
                            sumGold = resultSet4.getInt("sumgold");
                        }
                    }
                    return sumGold;
            }
            return 0;
        }

        public String getName(int id) throws SQLException {
            Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
            String sql = "SELECT * FROM public.users WHERE users.id='" + id + "';";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    return resultSet.getString("name");
                }
            }
            return null;
        }

        @FXML
        String[] onClickExecute(String action, String company, int id_user, float price, int quantity) throws SQLException {
            String[] args = new String[2];
            if (action.equals("BUY_BID") &&
                    quantity * price <= getSumRubles(id_user) ||
                    action.equals("SELL_BID") && quantity <= getNumberCurrentCompany(company, getName(id_user))) {
                if (!new OfferDaoImpl().isExistPrice(price, id_user)) {
                    String query = "INSERT INTO public.offers (id_user, company, action, price, quantity) VALUES (?, ?, ?, ?, ?);";
                    try (PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(query)) {
                        preparedStatement.setInt(1, id_user);
                        preparedStatement.setString(2, company);
                        preparedStatement.setString(3, action);
                        preparedStatement.setFloat(4, price);
                        preparedStatement.setInt(5, quantity);
                        preparedStatement.execute();
                    } catch (SQLException exception) {
                        throw new RuntimeException(exception);
                    }
                } else {
                    String sql = "UPDATE offers SET quantity=quantity+? WHERE id_user=? AND company=? AND action=? AND price=?";
                    try {
                        PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(sql);
                        preparedStatement.setInt(1, quantity);
                        preparedStatement.setInt(2, id_user);
                        preparedStatement.setString(3, company);
                        preparedStatement.setString(4, action);
                        preparedStatement.setFloat(5, price);
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (action.equals("BUY_BID")) {
                    String sql = "UPDATE users SET sumrubles=sumrubles-? WHERE id=?";
                    try {
                        PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(sql);
                        preparedStatement.setFloat(1, quantity * price);
                        preparedStatement.setInt(2, id_user);
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else if (action.equals("SELL_BID")) {
                    String sql = "UPDATE users SET " +
                            "sum" + company.toLowerCase() + "=" +
                            "sum" + company.toLowerCase()
                            + "-" + quantity +
                            " WHERE id=?";
                    try {
                        PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(sql);
                        preparedStatement.setInt(1, id_user);
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (action.equals("BUY_BID") &&
                    quantity * price > getSumRubles(id_user)) {
                args[0] = "ОШИБКА :(";
                args[1] = "не хватает денег для выставления данной заявки";
            } else if (action.equals("SELL_BID") &&
                    quantity > getNumberCurrentCompany(company, getName(id_user))) {

                args[0] = "ОШИБКА :(";
                args[1] = "не хватает акций для выставления данной заявки";
            } else if (action.equals("BUY")) {
                List<Integer[]> prices = new ArrayList<>();
                try {
                    Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
                    String sql = "SELECT id_user, price, quantity FROM public.offers WHERE offers.company='" + company + "' AND offers.action='SELL_BID';";
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
                    int count = quantity;
                    int i = 0;
                    for (Integer[] pricee : prices) {
                        if (i < count) {
                            sum += pricee[1];
                            i++;
                        } else {
                            break;
                        }
                    }
                    //рассмотрим 4 случая хватает ли денег и заявок или нет
                    //1 случай - хватает и того и того
                    //2 - деньги есть, заявок недостаточно
                    //3 - денег нет
                    int size = prices.size();
                    if (size == 0) {
                        args[0] = "ПРЕДУПРЕЖДЕНИЕ :|";
                        args[1] = "Такие акции сейчас недоступны для покупки";
                    } else {
                        if (getSumRubles(id_user) >= sum && quantity <= size) {
                            for (int r = 0; r < count; r++) {
                                changeMoneyOnPaper(prices.get(r)[0], prices.get(r)[1], "SELL_BID", company, id_user);
                            }
                            args[0] = "ОТЛИЧНО :)";
                            args[1] = "ты молодец, акции куплены";
                        } else if (getSumRubles(id_user) >= sum && quantity > size) {
                            // покупаем все возможные заявки и говорим пользователю что купили все возможные и часть денег обратно и оповещаем
                            for (int k = 0; k < size; k++) {
                                changeMoneyOnPaper(prices.get(k)[0], prices.get(k)[1], "SELL_BID", company, id_user);
                            }
                            args[0] = "ПРЕДУПРЕЖДЕНИЕ :|";
                            args[1] = "куплены не все акции, но часть куплена";
                        } else if (getSumRubles(id_user) < sum) {
                            args[0] = "ОШИБКА :(";
                            args[1] = "не хватает денег для совершения данной операции";
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (action.equals("SELL")) {
                try {
                    List<Integer[]> prices = new ArrayList<>();
                    //формируем массив всех заявок по выбранному 1 из 2 условий
                    Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
                    String sql = "SELECT id_user, price, quantity FROM public.offers WHERE offers.company='" + company + "' AND offers.action='BUY_BID';";
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
                    int size = prices.size();
                    if (size == 0) {
                        args[0] = "ПРЕДУПРЕЖДЕНИЕ :|";
                        args[1] = "Такие акции сейчас недоступны для покупки";
                    }
                    Collections.sort(prices, Comparator.comparingInt(arr -> arr[1]));
                    Collections.reverse(prices);
                    int sum = 0;
                    int count = quantity;
                    int i = 0;
                    for (Integer[] pricee : prices) {
                        if (i < count) {
                            sum += pricee[1];
                            i++;
                        } else {
                            break;
                        }
                    }
                    //рассмотрим 4 случая хватает ли акций и заявок или нет
                    //1 случай - хватает и того и того
                    //2 - акции есть, заявок недостаточно
                    //3 - акций нет
                    if (getNumberCurrentCompany(company, getName(id_user)) >= quantity &&
                            quantity <= size) {
                        for (int r = 0; r < count; r++) {
                            changePaperOnMoney(prices.get(r)[0], prices.get(r)[1], "BUY_BID", company, id_user);
                        }
                        args[0] = "ОТЛИЧНО :)";
                        args[1] = "ты молодец, акции проданы";
                    } else if (getNumberCurrentCompany(company, getName(id_user)) >= quantity && quantity > size) {
                        // продаем все возможные заявки и говорим пользователю что продали все возможные и часть денег обратно и оповещаем
                        for (int k = 0; k < size; k++) {
                            changePaperOnMoney(prices.get(k)[0], prices.get(k)[1], "BUY_BID", company, id_user);
                        }
                        args[0] = "ПРЕДУПРЕЖДЕНИЕ :|";
                        args[1] = "проданы не все акции, но часть продана";
                    } else {
                        args[0] = "ОШИБКА :(";
                        args[1] = "не хватает денег/акций для совершения данной операции";
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
            return args;
        }

        static void changePaperOnMoney(int id_user, float price, String action, String company, int cur_user_id) {
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
                preparedStatement.setString(2, company);
                preparedStatement.setString(3, action);
                preparedStatement.setFloat(4, price);
                preparedStatement.setInt(5, id_user);
                preparedStatement.setString(6, company);
                preparedStatement.setString(7, action);
                preparedStatement.setFloat(8, price);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String sql2 = null;
            switch (company) {
                case "SBER":
                    sql2 = "UPDATE users SET sumsber=sumsber+1 WHERE id=?";
                    break;
                case "ROSNEFT":
                    sql2 = "UPDATE users SET sumrosneft=sumrosneft+1 WHERE id=?";
                    break;
                case "AEROFLOT":
                    sql2 = "UPDATE users SET sumaeroflot=sumaeroflot+1 WHERE id=?";
                    break;
                case "GOLD":
                    sql2 = "UPDATE users SET sumgold=sumgold+1 WHERE id=?";
                    break;
            }
            try {
                PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(sql2);
                preparedStatement.setInt(1, id_user);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            //теперь нам понижаем баланс и повышаем кол-во акций
            String sql3 = null;
            switch (company) {
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
                preparedStatement.setInt(2, cur_user_id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        static void changeMoneyOnPaper(int id_user, float price, String action, String company, int cur_user_id) {
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
                preparedStatement.setString(2, company);
                preparedStatement.setString(3, action);
                preparedStatement.setFloat(4, price);
                preparedStatement.setInt(5, id_user);
                preparedStatement.setString(6, company);
                preparedStatement.setString(7, action);
                preparedStatement.setFloat(8, price);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            //пополняем баланс у продавца(мы покупатели) в users
            String sql2 = "UPDATE users SET sumrubles=sumrubles+? WHERE id=?";
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
            switch (company) {
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
                preparedStatement.setInt(2, cur_user_id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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
            else if (action.equals("BUY_BID")) return Collections.max(pricec);
            else return 0;
        }

        List<Integer[]> searchPriceAndQuantity(String action, String company) throws SQLException {
            List<Integer[]> list = new ArrayList<>();
            Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
            String sql = "SELECT price, quantity FROM public.offers WHERE offers.company='" + company + "' AND offers.action='" + action + "';";
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
            if (action.equals("BUY_BID")) Collections.reverse(list);
            return list;
        }

        void signInOrUpUser(String name) {
            User user = null;
            if (!isExistUser(name)) {
                String query = "INSERT INTO public.users (name, sumrubles, sumsber, sumrosneft, sumaeroflot, sumgold) VALUES (?, ?, ?, ?, ?, ?);";
                try (PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(query)) {
                    preparedStatement.setString(1, name);
                    preparedStatement.setInt(2, 10000);
                    preparedStatement.setInt(3, 10);
                    preparedStatement.setInt(4, 10);
                    preparedStatement.setInt(5, 10);
                    preparedStatement.setInt(6, 10);
                    preparedStatement.execute();
                } catch (SQLException exception) {
                    throw new RuntimeException(exception);
                }
            }
            try {
                Statement statement = databaseConnection.createStatement();
                String sql = "SELECT * from public.users WHERE name='" + name + "';";
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
        }
    }

    public static void main(String[] args) {
        int port = 5555; // Порт, на котором сервер будет прослушивать подключения
        Server server = new Server(port);
        server.start();
    }
}