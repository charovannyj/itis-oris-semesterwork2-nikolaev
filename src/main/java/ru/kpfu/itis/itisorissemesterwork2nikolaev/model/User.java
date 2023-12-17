package ru.kpfu.itis.itisorissemesterwork2nikolaev.model;

import ru.kpfu.itis.itisorissemesterwork2nikolaev.DatabaseConnectionUtil;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.InvestmentsApplication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class User {
    String name;
    int sumRubles;
    int sumSber;
    int sumRosneft;
    int sumAeroflot;
    int sumGold;
    int id;

    public int getId() throws SQLException {
        int id = 0;
        Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
        String sql = "SELECT * FROM public.users WHERE users.name='" + InvestmentsApplication.database.currentName + "';";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet != null) {
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        }
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User(String name, int sumRubles, int sumSber, int sumRosneft, int sumAeroflot, int sumGold) {
        this.name = name;
        this.sumRubles = sumRubles;
        this.sumSber = sumSber;
        this.sumRosneft = sumRosneft;
        this.sumAeroflot = sumAeroflot;
        this.sumGold = sumGold;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSumRubles() {
        return sumRubles;
    }

    public void setSumRubles(int sumRubles) {
        this.sumRubles = sumRubles;
    }

    public int getSumSber() {
        return sumSber;
    }

    public void setSumSber(int sumSber) {
        this.sumSber = sumSber;
    }

    public int getSumRosneft() {
        return sumRosneft;
    }

    public void setSumRosneft(int sumRosneft) {
        this.sumRosneft = sumRosneft;
    }

    public int getSumAeroflot() {
        return sumAeroflot;
    }

    public void setSumAeroflot(int sumAeroflot) {
        this.sumAeroflot = sumAeroflot;
    }

    public int getSumGold() {
        return sumGold;
    }

    public void setSumGold(int sumGold) {
        this.sumGold = sumGold;
    }
}
