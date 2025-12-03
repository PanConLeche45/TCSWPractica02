/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.uv.tcswpractica02;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sinoe
 */
public class ConexionDB {

    private static ConexionDB cx = null;

    public static ConexionDB getInstance() {
        if (cx == null) {
            cx = new ConexionDB();
        }
        return cx;
    }

    private String dbName = "ejemplo";
    private String url = "jdbc:postgresql://localhost:5432/" + dbName;
    private String usr = "postgres";
    private String pwd = "710563";

    private Connection con = null;

    private ConexionDB() {
        try {
            con = DriverManager.getConnection(url, usr, pwd);
            Logger.getLogger(ConexionDB.class.getName()).log(Level.INFO, "SE CONECTÓ A BD {0}", dbName);
            ensureTable();

        } catch (SQLException ex) {
            if (ex.getSQLState() != null && ex.getSQLState().equals("3D000")) {
                Logger.getLogger(ConexionDB.class.getName()).log(Level.WARNING, "LA BD {0} NO EXISTE. CREANDO...", dbName);
                createDatabase();
                try {
                    con = DriverManager.getConnection(url, usr, pwd);
                    Logger.getLogger(ConexionDB.class.getName()).log(Level.INFO, "SE CONECTÓ A BD {0}", dbName);
                    ensureTable();
                } catch (SQLException ex2) {
                    Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex2);
                }
            } else {
                Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void createDatabase() {
        String urlDefault = "jdbc:postgresql://localhost:5432/postgres"; // conecta a la BD por defecto
        try (Connection tmp = DriverManager.getConnection(urlDefault, usr, pwd); Statement st = tmp.createStatement()) {
            st.executeUpdate("CREATE DATABASE " + dbName);
            Logger.getLogger(ConexionDB.class.getName()).log(Level.INFO, "BASE DE DATOS {0} CREADA", dbName);
        } catch (SQLException e) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void ensureTable() {
        String ddl = """
            CREATE TABLE IF NOT EXISTS empleados2 (
              clave BIGINT PRIMARY KEY,
              nombre VARCHAR(100) NOT NULL,
              direccion VARCHAR(200),
              telefono VARCHAR(30)
            );
        """;
        try (Statement st = con.createStatement()) {
            st.execute(ddl);
            Logger.getLogger(ConexionDB.class.getName()).log(Level.INFO, "TABLA EMPLEADOS2 LISTA");
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, "ERROR CREANDO TABLA", ex);
        }
    }

    public ResultSet select(String sql) {
        Statement st = null;
        try {
            st = con.createStatement();
            return st.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean execute(String sql) {
        Statement st = null;
        try {
            st = con.createStatement();
            st.execute(sql);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean execute (TransactionDB t) {
        return t.execute(con);
    }
}
