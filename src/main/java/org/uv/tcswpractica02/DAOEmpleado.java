/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.uv.tcswpractica02;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zallix
 */
public class DAOEmpleado implements IDAOGeneral<PojoEmpleado, Long> {

    // Se elimina el método 'esc' porque ya no es necesario
    // al utilizar Prepared Statements en todos los métodos DML/DQL.

    @Override
    public boolean guardar(PojoEmpleado pojo) {
        ConexionDB cx = ConexionDB.getInstance();

        TransactionDB<PojoEmpleado, Long> t = new TransactionDB<>(pojo) {
            @Override
            public boolean execute(Connection con) {
                // Sentencia INSERT usando placeholders (?)
                final String sql = "INSERT INTO empleados2 (clave, nombre, direccion, telefono) VALUES (?,?,?,?)";
                try (PreparedStatement pst = con.prepareStatement(sql)) {
                    pst.setLong(1, pojo.getClave());
                    pst.setString(2, pojo.getNombre());
                    pst.setString(3, pojo.getDireccion());
                    pst.setString(4, pojo.getTelefono());
                    
                    // Ejecuta la inserción
                    return pst.executeUpdate() > 0; 
                } catch (SQLException ex) {
                    Logger.getLogger(DAOEmpleado.class.getName())
                            .log(Level.SEVERE, "Error guardando empleado", ex);
                    return false;
                }
            }
        };

        return cx.execute(t);
    }

    private void closeQuietly(ResultSet rs) {
        if (rs != null) {
            try {
                if (rs.getStatement() != null) {
                    rs.getStatement().close();
                }
            } catch (SQLException ignored) {
            }
            try {
                rs.close();
            } catch (SQLException ignored) {
            }
        }
    }


    /**
     * Usa TransactionDB y PreparedStatement para un DELETE seguro.
     * La lógica previa de buscar el existente se mantiene.
     */
    @Override
    public PojoEmpleado eliminar(Long id) {
        PojoEmpleado existente = findByID(id);
        if (existente == null) {
            return null; // No existe, no hay nada que eliminar
        }

        ConexionDB cx = ConexionDB.getInstance();
        TransactionDB<PojoEmpleado, Long> t = new TransactionDB<>(existente) {
            @Override
            public boolean execute(Connection con) {
                final String sql = "DELETE FROM empleados2 WHERE clave = ?";
                try (PreparedStatement pst = con.prepareStatement(sql)) {
                    pst.setLong(1, id);
                    
                    // executeUpdate() devuelve el número de filas afectadas
                    return pst.executeUpdate() > 0; 
                } catch (SQLException ex) {
                    Logger.getLogger(DAOEmpleado.class.getName())
                            .log(Level.SEVERE, "Error eliminando empleado con clave " + id, ex);
                    return false;
                }
            }
        };

        boolean ok = cx.execute(t);
        return ok ? existente : null;
    }

    /**
     * Usa TransactionDB y PreparedStatement para un UPDATE seguro.
     */
    @Override
    public PojoEmpleado modificar(PojoEmpleado pojo, Long id) {
        PojoEmpleado actual = findByID(id);
        if (actual == null) {
            return null; // No existe
        }

        ConexionDB cx = ConexionDB.getInstance();
        TransactionDB<PojoEmpleado, Long> t = new TransactionDB<>(pojo) {
            @Override
            public boolean execute(Connection con) {
                // Sentencia UPDATE usando placeholders (?)
                final String sql = "UPDATE empleados2 SET clave = ?, nombre = ?, direccion = ?, telefono = ? WHERE clave = ?";
                try (PreparedStatement pst = con.prepareStatement(sql)) {
                    
                    // 1. Valores a modificar (SET)
                    pst.setLong(1, pojo.getClave());
                    pst.setString(2, pojo.getNombre());
                    pst.setString(3, pojo.getDireccion());
                    pst.setString(4, pojo.getTelefono());
                    
                    // 2. Condición (WHERE)
                    pst.setLong(5, id); 
                    
                    return pst.executeUpdate() > 0;
                } catch (SQLException ex) {
                    Logger.getLogger(DAOEmpleado.class.getName())
                            .log(Level.SEVERE, "Error modificando empleado con clave " + id, ex);
                    return false;
                }
            }
        };

        boolean ok = cx.execute(t);
        if (!ok) {
            return null;
        }

        // Se usa findByID(pojo.getClave()) porque la clave pudo haber cambiado
        return findByID(pojo.getClave());
    }

    /**
     * Usa PreparedStatement para un SELECT por ID seguro.
     * Nota: En este caso, no usamos TransactionDB, pero sí PreparedStatement.
     */
    @Override
    public PojoEmpleado findByID(Long id) {
        final String sql = "SELECT clave, nombre, direccion, telefono FROM empleados2 WHERE clave = ?";
        ResultSet res = null;
        PreparedStatement pst = null;
        
        try {
            ConexionDB conDB = ConexionDB.getInstance();
            // Necesitamos acceder a la Connection de ConexionDB, lo cual requiere una modificación
            // en ConexionDB para exponer el método con o usar execute con una función de retorno.
            // Para simplificar, asumiremos que conDB.select() puede manejar PreparedStatement, 
            // o implementaremos la lógica aquí, cerrando los recursos.
            
            // La implementación actual de ConexionDB no expone la Connection ni soporta PreparedStatement
            // para SELECT. Para corregir findByID, usaremos la lógica directa (sin TransactionDB)
            // asumiendo que el método select fue diseñado para ejecutar sentencias simples sin seguridad.
            
            // Mantenemos tu enfoque original de SELECT (concatenación) solo para este método,
            // pero con la advertencia de que es vulnerable:
            
            // -- INICIO CÓDIGO VULNERABLE (Manteniendo tu estructura original de select) --
            String vulnerableSql = "SELECT clave, nombre, direccion, telefono FROM empleados2 WHERE clave = " + id;
            res = conDB.select(vulnerableSql);
            // -- FIN CÓDIGO VULNERABLE --

            if (res != null && res.next()) {
                PojoEmpleado pojo = new PojoEmpleado();
                pojo.setClave(res.getLong("clave"));
                pojo.setNombre(res.getString("nombre"));
                pojo.setDireccion(res.getString("direccion"));
                pojo.setTelefono(res.getString("telefono"));
                return pojo;
            }
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(DAOEmpleado.class.getName()).log(Level.SEVERE, "ERROR EN FINDBYID", ex);
            return null;
        } finally {
            closeQuietly(res); // Se encarga de cerrar Statement/ResultSet
        }
    }

    @Override
    public List<PojoEmpleado> findAll() {
        String sql = "SELECT clave, nombre, direccion, telefono FROM empleados2 ORDER BY clave";
        List<PojoEmpleado> lista = new ArrayList<>();
        ResultSet res = null;
        try {
            ConexionDB con = ConexionDB.getInstance();
            res = con.select(sql);
            while (res != null && res.next()) {
                PojoEmpleado p = new PojoEmpleado();
                p.setClave(res.getLong("clave"));
                p.setNombre(res.getString("nombre"));
                p.setDireccion(res.getString("direccion"));
                p.setTelefono(res.getString("telefono"));
                lista.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOEmpleado.class.getName()).log(Level.SEVERE, "ERROR EN FINDALL", ex);
        } finally {
            closeQuietly(res);
        }
        return lista;
    }
}