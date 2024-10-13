package org.example.DBActions;
import java.util.ArrayList;

public interface DBBehavior<T> {
    ArrayList<T> getAll();
    T getById(String id);
    boolean add (T object);
    boolean update (T object);
    boolean delete (T object);
}
