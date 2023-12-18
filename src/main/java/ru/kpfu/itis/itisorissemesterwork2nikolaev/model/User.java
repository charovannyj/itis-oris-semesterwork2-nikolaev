package ru.kpfu.itis.itisorissemesterwork2nikolaev.model;

import ru.kpfu.itis.itisorissemesterwork2nikolaev.DatabaseConnectionUtil;
import ru.kpfu.itis.itisorissemesterwork2nikolaev.InvestmentsApplication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class User {
    int numberCurrentCompany;
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

    public int getNumberCurrentCompany(String company) throws SQLException {
        Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
        switch (company){
            case("SBER"):
                String sql1 = "SELECT sumsber FROM public.users WHERE users.name='" + InvestmentsApplication.database.currentUser.getName() + "';";
                ResultSet resultSet1 = statement.executeQuery(sql1);
                if (resultSet1 != null) {
                    while (resultSet1.next()) {
                        sumRubles = resultSet1.getInt("sumsber");
                    }
                }
                return sumRubles;
            case("ROSNEFT"):
                String sql2 = "SELECT sumrosneft FROM public.users WHERE users.name='" + InvestmentsApplication.database.currentUser.getName() + "';";
                ResultSet resultSet2 = statement.executeQuery(sql2);
                if (resultSet2 != null) {
                    while (resultSet2.next()) {
                        sumRosneft = resultSet2.getInt("sumrosneft");
                    }
                }
                return sumRosneft;
            case("AEROFLOT"):
                String sql3 = "SELECT sumaeroflot FROM public.users WHERE users.name='" + InvestmentsApplication.database.currentUser.getName() + "';";
                ResultSet resultSet3 = statement.executeQuery(sql3);
                if (resultSet3 != null) {
                    while (resultSet3.next()) {
                        sumAeroflot = resultSet3.getInt("sumaeroflot");
                    }
                }
                return sumAeroflot;
            case("GOLD"):
                String sql4 = "SELECT sumgold FROM public.users WHERE users.name='" + InvestmentsApplication.database.currentUser.getName() + "';";
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

    public void setNumberCurrentCompany(int numberCurrentCompany) {
        this.numberCurrentCompany = numberCurrentCompany;
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

    public int getSumRubles() throws SQLException {
        int sumRubles = 0;
        Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
        String sql = "SELECT * FROM public.users WHERE users.name='" + InvestmentsApplication.database.currentName + "';";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet != null) {
            while (resultSet.next()) {
                sumRubles = resultSet.getInt("sumrubles");
            }
        }
        return sumRubles;
    }

    public void setSumRubles(int sumRubles) {
        this.sumRubles = sumRubles;
    }

    public int getSumSber() throws SQLException {
        int sumSber = 0;
        Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
        String sql = "SELECT * FROM public.users WHERE users.name='" + InvestmentsApplication.database.currentName + "';";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet != null) {
            while (resultSet.next()) {
                sumSber = resultSet.getInt("sumsber");
            }
        }
        return sumSber;
    }

    public void setSumSber(int sumSber) {
        this.sumSber = sumSber;
    }

    public int getSumRosneft() throws SQLException {
        int sumRosneft = 0;
        Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
        String sql = "SELECT * FROM public.users WHERE users.name='" + InvestmentsApplication.database.currentName + "';";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet != null) {
            while (resultSet.next()) {
                sumRosneft = resultSet.getInt("sumrosneft");
            }
        }
        return sumRosneft;
    }

    public void setSumRosneft(int sumRosneft) {
        this.sumRosneft = sumRosneft;
    }

    public int getSumAeroflot() throws SQLException {
        int sumAeroflot = 0;
        Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
        String sql = "SELECT * FROM public.users WHERE users.name='" + InvestmentsApplication.database.currentName + "';";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet != null) {
            while (resultSet.next()) {
                sumAeroflot = resultSet.getInt("sumaeroflot");
            }
        }
        return sumAeroflot;
    }

    public void setSumAeroflot(int sumAeroflot) {
        this.sumAeroflot = sumAeroflot;
    }

    public int getSumGold() throws SQLException {
        int sumGold = 0;
        Statement statement = DatabaseConnectionUtil.getConnection().createStatement();
        String sql = "SELECT * FROM public.users WHERE users.name='" + InvestmentsApplication.database.currentName + "';";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet != null) {
            while (resultSet.next()) {
                sumGold = resultSet.getInt("sumgold");
            }
        }
        return sumGold;
    }

    public void setSumGold(int sumGold) {
        this.sumGold = sumGold;
    }
}
