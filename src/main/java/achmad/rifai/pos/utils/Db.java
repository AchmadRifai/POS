/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package achmad.rifai.pos.utils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AmMas
 */
public class Db {
    @SuppressWarnings("FieldMayBeFinal")
    private java.sql.Connection c;
    @SuppressWarnings("FieldMayBeFinal")
    private java.sql.Statement s;

    public Db(String host, String nm, int port, String user, String pass) throws SQLException {
        try {
            com.mysql.jdbc.Driver.class.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        } c = DriverManager.getConnection("jdbc:mysql://" + host + ':' + port + '/' + nm, user, pass);
        s = c.createStatement();
    }

    public List<Map<String, ValsOut>> hasil(String sql, Params...par) throws SQLException{
        List<Map<String, ValsOut>> l = new java.util.LinkedList<>();
        try (PreparedStatement p = prep(sql)) {
            fill(p, par);
            try (ResultSet r = p.executeQuery()) {
                while(r.next()){
                    Map<String, ValsOut> m = new java.util.HashMap<>();
                    for(int i = 0; i < r.getMetaData().getColumnCount(); i++){
                        String col = r.getMetaData().getColumnName(i);
                        ValsOut v = new ValsOut();
                        v.setType(r.getMetaData().getColumnType(i));
                        sett(r, v, col);
                        m.put(col, v);
                    } l.add(m);
                }
            }
        } return l;
    }

    public ResultSet hasil(String sql) throws SQLException{
        return s.executeQuery(sql);
    }

    public void exec(String sql, Params...par) throws SQLException{
        try (PreparedStatement p = prep(sql)) {
            fill(p, par);
            p.execute();
        }
    }

    public void exec(String sql) throws SQLException{
        s.execute(sql);
    }

    public void close() throws SQLException{
        s.close();
        c.close();
    }

    public Db() throws SQLException{
        try {
            org.sqlite.JDBC.class.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        } if(!Work.f.getParentFile().exists()) Work.f.getParentFile().mkdirs();
        c = DriverManager.getConnection("jdbc:sqlite:" + Work.f.getPath());
        s = c.createStatement();
    }

    public PreparedStatement prep(String sql) throws SQLException {
        return c.prepareStatement(sql);
    }

    private void fill(PreparedStatement p, Params[] par) throws SQLException {
        int i = 1;
        for(Params v:par){
            if(null!=v.getVal())switch(v.getType()){
                case Types.BIGINT:
                    p.setLong(i, (long) v.getVal());
                    break;
                case Types.BOOLEAN:
                    p.setBoolean(i, (boolean) v.getVal());
                    break;
                case Types.DATE:
                    p.setDate(i, (Date) v.getVal());
                    break;
                case Types.DECIMAL:
                    p.setBigDecimal(i, (BigDecimal) v.getVal());
                    break;
                case Types.DOUBLE:
                    p.setDouble(i, (double) v.getVal());
                    break;
                case Types.INTEGER:
                    p.setInt(i, (int) v.getVal());
                    break;
                case Types.TIME:
                    p.setTime(i, (Time) v.getVal());
                    break;
                case Types.TIMESTAMP:
                    p.setTimestamp(i, (Timestamp) v.getVal());
                    break;
                case Types.VARCHAR:
                    p.setString(i, (String) v.getVal());
                    break;
                case Types.BLOB:
                    p.setBlob(i, (InputStream) v.getVal());
                    break;
                default:p.setObject(i, v.getVal());
            } else p.setNull(i, v.getType());
            i++;
        }
    }

    private void sett(ResultSet r, ValsOut v, String col) throws SQLException {
        switch(v.getType()){
            case Types.BIGINT:
                v.setVal(r.getObject(col, Long.class));
                break;
            case Types.BOOLEAN:
                v.setVal(r.getObject(col, Boolean.class));
                break;
            case Types.DATE:
                v.setVal(r.getObject(col, Date.class));
                break;
            case Types.DECIMAL:
                v.setVal(r.getObject(col, BigDecimal.class));
                break;
            case Types.DOUBLE:
                v.setVal(r.getObject(col, Double.class));
                break;
            case Types.INTEGER:
                v.setVal(r.getObject(col, Integer.class));
                break;
            case Types.TIME:
                v.setVal(r.getObject(col, Time.class));
                break;
            case Types.TIMESTAMP:
                v.setVal(r.getObject(col, Timestamp.class));
                break;
            case Types.VARCHAR:
                v.setVal(r.getObject(col, String.class));
                break;
            case Types.BLOB:
                v.setVal(r.getObject(col, InputStream.class));
                break;
            default:
                v.setVal(r.getObject(col));
        }
    }
}
