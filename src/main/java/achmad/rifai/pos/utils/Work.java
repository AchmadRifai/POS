/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package achmad.rifai.pos.utils;

import achmad.rifai.pos.entitas.Transaksi;
import achmad.rifai.pos.entitas.dao.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AmMas
 */
public class Work {
    public static java.io.File f=new java.io.File(System.getProperty("user.home") + "/.pos/conf.db");

    public static void awalan() throws SQLException {
        Db d = new Db();
        d.exec("create table if not exists rusak(tgl bigint not null,msg text not null,src text not null)");
        d.exec("create table if not exists koneksi(host text not null,nm text not null,port int not null," +
                "uid text not null,pwd text not null)");
        d.close();
    }

    public static void buatDB() throws SQLException {
        Db d=loadDb();
        DAO dao=new DAOPerusahaan(d);
        dao.create();
        dao=new DAOJabatan(d);
        dao.create();
        dao=new DAORegister(d);
        dao.create();
        dao=new DAOPegawai(d);
        dao.create();
        dao=new DAOTransaksi(d);
        dao.create();
        d.close();
    }

    public static Db loadDb() throws SQLException {
        return new Db();
    }

    public static void hindar(Exception ex) {
        long l = System.currentTimeMillis();
        String src="";
        try (java.io.StringWriter w = new java.io.StringWriter(); java.io.PrintWriter o = new java.io.PrintWriter(w)) {
            ex.printStackTrace(o);
            w.write(src);
        } catch (IOException ex1) {
            Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex1);
        } try {
            Db d = new Db();
            d.exec("insert into rusak values(?,?,?)", new Params(l, Types.BIGINT), new Params(ex.getMessage(), Types.VARCHAR),
                    new Params(src, Types.VARCHAR));
            d.close();
        } catch (SQLException ex1) {
            Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    public static void tataRegister() {
        try {
            Db d=loadDb();
            d.close();
        } catch (SQLException e) {
            hindar(e);
        }
    }
}
