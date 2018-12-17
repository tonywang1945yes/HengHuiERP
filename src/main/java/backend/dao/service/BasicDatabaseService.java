package backend.dao.service;

import backend.enums.resultMessage.DatabaseRM;

import java.util.ArrayList;

public interface BasicDatabaseService<T> {
    DatabaseRM add(T t0);
    DatabaseRM delete(String keyValue);
    DatabaseRM update(T t0);
    T findByKey(String keyValue);
    boolean checkKeyExists(String keyValue);
    ArrayList<T> getAllObjects();
    ArrayList<T> executeQuerySql(String sqlString); //这种写法并不防止sql注入攻击
}
