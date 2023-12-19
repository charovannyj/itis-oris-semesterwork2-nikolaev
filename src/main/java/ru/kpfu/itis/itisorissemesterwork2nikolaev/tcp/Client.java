package ru.kpfu.itis.itisorissemesterwork2nikolaev.tcp;


import ru.kpfu.itis.itisorissemesterwork2nikolaev.DatabaseConnectionUtil;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Client(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User loginUser(String username) {
        try {
            output.writeObject("LOGIN " + username); // отправляем запрос на вход
            // получаем ответ от сервера
            return (User) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] execute(String action, String company, int id_user, float price, int quantity) {
        try {
            output.writeObject("EXECUTE " + action + " " + company + " " + id_user + " " + price + " " + quantity); // отправляем запрос на вход

            String[] list = (String[]) input.readObject();

            return list;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }

    public List<List> sendCompany(String company) {
        try {
            output.writeObject("COMPANY " + company); // отправляем запрос на вход
            // получаем ответ от сервера
            List<Integer[]> listBuy = (List<Integer[]>) input.readObject();
            List<Integer[]> listSell = (List<Integer[]>) input.readObject();
            List<List> list = new ArrayList<>();
            list.add(listBuy);
            list.add(listSell);
            return list;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public float searchPrice(String action, String company) {
        try {
            output.writeObject("BUYSELLPRICE " + company + " " + action); // отправляем запрос на вход
            // получаем ответ от сервера
            float number = (float) input.readObject();
            return number;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public void closeConnection() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Float getSumRubles(String name) throws IOException, ClassNotFoundException {
        try{
            output.writeObject("GETSUMRUBLES " + name);
            Float sum = (Float) input.readObject();
            return sum;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}