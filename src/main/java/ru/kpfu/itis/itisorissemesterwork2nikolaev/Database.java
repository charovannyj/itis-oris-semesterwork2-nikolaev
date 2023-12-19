package ru.kpfu.itis.itisorissemesterwork2nikolaev;

import javafx.scene.control.Alert;
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
