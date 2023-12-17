package ru.kpfu.itis.itisorissemesterwork2nikolaev.model;

public class Offer {
    int idUser;
    String name;
    String action;
    float price;
    int quantity;

    public Offer(int idUser, String name, String action, float price, int quantity) {
        this.idUser = idUser;
        this.name = name;
        this.action = action;
        this.price = price;
        this.quantity = quantity;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
