/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.tikapeharjoitustyo2.dao;

import com.company.tikapeharjoitustyo2.database.Database;
import com.company.tikapeharjoitustyo2.domain.Annos;
import com.company.tikapeharjoitustyo2.domain.AnnosRaakaAine;
import com.company.tikapeharjoitustyo2.domain.RaakaAine;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author alecsiikaluoma
 */
public class AnnosRaakaAineDao implements Dao<AnnosRaakaAine, Integer> {
    
    private Database database;
    private Dao<Annos, Integer> annosDao;
    private Dao<RaakaAine, Integer> raakaAineDao;
    
    public AnnosRaakaAineDao(Database db, Dao<Annos, Integer> annosDao, Dao<RaakaAine, Integer> raakaAineDao) {
        this.database = db;
        this.annosDao = annosDao;
        this.raakaAineDao = raakaAineDao;
    }
    
        @Override
    public void delete(Integer key) throws SQLException {
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM RaakaAine WHERE id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
        }
                
    }

    @Override
    public AnnosRaakaAine findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public AnnosRaakaAine findByIds(Integer raakaAine, Integer annos) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine WHERE raaka_aine_id = ? AND annos_id = ?");
            stmt.setInt(1, raakaAine);
            stmt.setInt(2, annos);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new AnnosRaakaAine(annosDao.findOne(annos), raakaAineDao.findOne(raakaAine), result.getInt("jarjestys"), result.getString("maara"), result.getString("ohje"));
        }
    }
    
    public List<AnnosRaakaAine> findAll(Integer annos) throws SQLException {
        
        List<AnnosRaakaAine> aineet = new ArrayList<>();
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine WHERE annos_id = ?");
            stmt.setInt(1, annos);
            
            ResultSet result = stmt.executeQuery();
            
            while(result.next()) {
                Annos an = annosDao.findOne(annos);
                RaakaAine ra = raakaAineDao.findOne(result.getInt("raaka_aine_id"));
                
                aineet.add(new AnnosRaakaAine(an, ra, result.getInt("jarjestys"), result.getString("maara"), result.getString("ohje")));
            }
        }
        
        return aineet;
        
    }

    @Override
    public AnnosRaakaAine saveOrUpdate(AnnosRaakaAine object) throws SQLException {
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO AnnosRaakaAine (raaka_aine_id, annos_id, jarjestys, maara, ohje"
                    + ") VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, object.getRaakaAine().getId());
            stmt.setInt(2, object.getAnnos().getId());
            stmt.setInt(3, object.getJarjestys());
            stmt.setString(4, object.getMaara());
            stmt.setString(5, object.getOhje());
            
            stmt.executeUpdate();
        }

        return findByIds(object.getRaakaAine().getId(), object.getAnnos().getId());

    }
    
    public int count(Integer raakaAine) {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) AS total FROM AnnosRaakaAine WHERE raaka_aine_id = ?");
            stmt.setInt(1, raakaAine);
            
            ResultSet result = stmt.executeQuery();
            
            return result.getInt("total");
        } catch(Exception e) {
            return 0;
        }
    }

    @Override
    public List<AnnosRaakaAine> findAll() throws SQLException {
        List<AnnosRaakaAine> aineet = new ArrayList<>();
        
        try(Connection conn = database.getConnection();
            ResultSet results = conn.prepareStatement("SELECT * FROM RaakaAine;").executeQuery();) {
            while(results.next()) {
            }
        }
        
        return aineet;
    }

}