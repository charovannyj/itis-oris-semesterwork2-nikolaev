package ru.kpfu.itis.itisorissemesterwork2nikolaev.dao.impl;

import ru.kpfu.itis.itisorissemesterwork2nikolaev.DatabaseConnectionUtil;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.InvestmentsApplication;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.dao.Dao;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.model.Offer;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements Dao<User> {
    private final Connection connection = DatabaseConnectionUtil.getConnection();

    @Override
    public User get(int id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM public.users;";
            ResultSet resultSet = statement.executeQuery(sql);
            List<User> users = new ArrayList<>();
            if (resultSet != null) {
                while (resultSet.next()) {
                    users.add(
                            new User(
                                    resultSet.getInt("id"),
                                    resultSet.getString("name"),
                                    resultSet.getInt("sumrubles"),
                                    resultSet.getInt("sumsber"),
                                    resultSet.getInt("sumrosneft"),
                                    resultSet.getInt("sumaeroflot"),
                                    resultSet.getInt("sumgold")
                            )
                    );
                }
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(User user) {
        String query = "INSERT INTO public.users (name, sumrubles, sumsber, sumrosneft, sumaeroflot, sumgold) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = DatabaseConnectionUtil.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setFloat(2, user.getSumRubles());
            preparedStatement.setInt(3, user.getSumSber());
            preparedStatement.setInt(4, user.getSumRosneft());
            preparedStatement.setInt(5, user.getSumAeroflot());
            preparedStatement.setInt(6, user.getSumGold());
            preparedStatement.execute();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}