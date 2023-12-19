package ru.kpfu.itis.itisorissemesterwork2nikolaev.dao.impl;

import ru.kpfu.itis.itisorissemesterwork2nikolaev.DatabaseConnectionUtil;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.InvestmentsApplication;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.dao.Dao;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.model.Offer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OfferDaoImpl implements Dao<Offer> {
    private final Connection connection = DatabaseConnectionUtil.getConnection();
    @Override
    public Offer get(int id) {
        return null;
    }
    public boolean isExistPrice(double price, int id_user){
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * from public.offers WHERE offers.company='"+InvestmentsApplication.database.currentCompany+"' AND offers.action='"+InvestmentsApplication.database.currentAction+"' AND offers.price=" + price +" AND offers.id_user='"+InvestmentsApplication.database.currentIdUser+"';";
            ResultSet resultSet = statement.executeQuery(sql);
            List<Offer> offers = new ArrayList<>();
            if (resultSet != null) {
                while (resultSet.next()) {
                    offers.add(
                            new Offer(
                                    resultSet.getInt("id_user"),
                                    resultSet.getString("company"),
                                    resultSet.getString("action"),
                                    resultSet.getFloat("price"),
                                    resultSet.getInt("quantity")
                            )
                    );
                }
            }
            return offers.size()>0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Offer> getAll(String name, String action) {
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * from public.offers WHERE offers.name="+name+" AND offers.action="+action+";";
            ResultSet resultSet = statement.executeQuery(sql);
            List<Offer> offers = new ArrayList<>();
            if (resultSet != null) {
                while (resultSet.next()) {
                    offers.add(
                            new Offer(
                                    resultSet.getInt("id"),
                                    resultSet.getString("name"),
                                    resultSet.getString("action"),
                                    resultSet.getFloat("price"),
                                    resultSet.getInt("quantity")
                            )
                    );
                }
            }
            return offers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void save(Offer offer) {
        String query = "INSERT INTO public.offers (name, action, price, quantity) VALUES (?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, offer.getName());
            preparedStatement.setString(2, offer.getAction());
            preparedStatement.setFloat(3, offer.getPrice());
            preparedStatement.setInt(4, offer.getQuantity());
            preparedStatement.execute();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

}

