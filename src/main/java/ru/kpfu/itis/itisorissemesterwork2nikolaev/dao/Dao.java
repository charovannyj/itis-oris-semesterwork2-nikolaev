package ru.kpfu.itis.itisorissemesterwork2nikolaev.dao;

import java.util.List;

public interface Dao<T> {

    T get(int id);

    List<T> getAll(String name, String action);

    void save (T t);
}