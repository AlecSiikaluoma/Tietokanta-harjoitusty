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
public class RaakaAineDao implements Dao<RaakaAine, Integer> {
    
    private Database database;
    
    public RaakaAineDao(Database db) {
        this.database = db;
    }
    
    @Override
    public List<RaakaAine> findAll() throws SQLException {
        List<RaakaAine> aineet = new ArrayList<>();
        
        try(Connection conn = database.getConnection();
                ResultSet results = conn.prepareStatement("SELECT * FROM RaakaAine;").executeQuery();) {
            while(results.next()) {
                aineet.add(new RaakaAine(results.getInt("id"), results.getString("nimi")));
            }
        }
        
        return aineet;
    }

    @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RaakaAine WHERE id = ?");
            stmt.setInt(1, key);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new RaakaAine(result.getInt("id"), result.getString("nimi"));
        }
    }
    
    private RaakaAine findByName(String nimi) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM RaakaAine WHERE nimi = ?");
            stmt.setString(1, nimi);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new RaakaAine(result.getInt("id"), result.getString("nimi"));
        }
    }

    @Override
    public RaakaAine saveOrUpdate(RaakaAine object) throws SQLException {
        RaakaAine etsitty = findByName(object.getNimi());
        
        if(etsitty != null) {
            return etsitty;
        }
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO RaakaAine (nimi) VALUES (?)");
            stmt.setString(1, object.getNimi());
            stmt.executeUpdate();
        }
        
        return findByName(object.getNimi());
        
    }

    @Override
    public void delete(Integer key) throws SQLException {
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM RaakaAine WHERE id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement("DELETE FROM AnnosRaakaAine WHERE raaka_aine_id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
        }
                
    }
    
}
