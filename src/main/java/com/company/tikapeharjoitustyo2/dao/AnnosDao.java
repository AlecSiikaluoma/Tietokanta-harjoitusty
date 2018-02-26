/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.tikapeharjoitustyo2.dao;

import com.company.tikapeharjoitustyo2.database.Database;
import com.company.tikapeharjoitustyo2.domain.Annos;
import com.company.tikapeharjoitustyo2.domain.RaakaAine;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alecsiikaluoma
 */
public class AnnosDao implements Dao<Annos, Integer> {
    
    private Database database;
    
    public AnnosDao(Database db) {
        this.database = db;
    }
    
    @Override
    public List<Annos> findAll() throws SQLException {
        List<Annos> annokset = new ArrayList<>();
        
        try(Connection conn = database.getConnection();
                ResultSet results = conn.prepareStatement("SELECT * FROM Annos;").executeQuery();) {
            while(results.next()) {
                annokset.add(new Annos(results.getInt("id"), results.getString("nimi")));
            }
        }
        
        return annokset;
    }
    
    @Override
    public Annos findOne(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Annos WHERE id = ?");
            stmt.setInt(1, key);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Annos(result.getInt("id"), result.getString("nimi"));
        }
    }
    
    private Annos findByName(String nimi) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM Annos WHERE nimi = ?");
            stmt.setString(1, nimi);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Annos(result.getInt("id"), result.getString("nimi"));
        }
    }

    
    @Override
    public Annos saveOrUpdate(Annos object) throws SQLException {
        Annos etsitty = findByName(object.getNimi());

        if (etsitty != null) {
            return etsitty;
        }

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Annos (nimi) VALUES (?)");
            stmt.setString(1, object.getNimi());
            stmt.executeUpdate();
        }

        return findByName(object.getNimi());
    }

    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Annos WHERE id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement("DELETE FROM AnnosRaakaAine WHERE annos_id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
        }
    }
    
}
