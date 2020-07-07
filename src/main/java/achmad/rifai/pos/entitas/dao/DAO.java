package achmad.rifai.pos.entitas.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T,W> {
    void create() throws SQLException;
    void drop() throws SQLException;
    void clean() throws SQLException;
    T one(W w) throws SQLException;
    List<T> all() throws SQLException;
    void insert(T v) throws SQLException;
    void update(W w, T v) throws SQLException;
    void delete(W w) throws SQLException;
}
