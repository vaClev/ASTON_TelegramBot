package org.example.DBActions;
import java.util.ArrayList;

public interface DBBehavior<T, ID> {
    ArrayList<T> getAll();
    T getById(ID id);
    boolean insert (T object);
    boolean update (T object);
    boolean delete (T object);
}
