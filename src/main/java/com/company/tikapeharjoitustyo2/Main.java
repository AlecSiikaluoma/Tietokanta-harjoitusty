package com.company.tikapeharjoitustyo2;


import com.company.tikapeharjoitustyo2.dao.AnnosDao;
import com.company.tikapeharjoitustyo2.dao.AnnosRaakaAineDao;
import com.company.tikapeharjoitustyo2.dao.RaakaAineDao;
import com.company.tikapeharjoitustyo2.database.Database;
import com.company.tikapeharjoitustyo2.domain.Annos;
import com.company.tikapeharjoitustyo2.domain.AnnosRaakaAine;
import com.company.tikapeharjoitustyo2.domain.RaakaAine;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alecsiikaluoma
 */
public class Main {
    
    public static void main(String[] args) throws Exception {
        
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }

        Database database = new Database("jdbc:sqlite:testi.db");
        AnnosDao annokset = new AnnosDao(database);
        RaakaAineDao ainekset = new RaakaAineDao(database);
        AnnosRaakaAineDao annoksetAinekset = new AnnosRaakaAineDao(database, annokset, ainekset);
        
        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annokset", annokset.findAll());
            
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/ainekset", (req, res) -> {
           HashMap map = new HashMap<>();
           map.put("ainekset", ainekset.findAll()); 
          
           return new ModelAndView(map, "ainekset");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/ainekset", (req, res) -> {
            String nimi = req.queryParams("aines");
            
            ainekset.saveOrUpdate(new RaakaAine(-1, nimi));
            
            res.redirect("/ainekset");
            return "";
        });
        
        Spark.get("/ainekset/:id/poista", (req, res) -> {
            Integer taskId = Integer.parseInt(req.params(":id"));
            
            ainekset.delete(taskId);
            
            res.redirect("/ainekset");
            
            return "";
        });
        
        Spark.get("/smoothiet", (req, res) -> {
           HashMap map = new HashMap<>();
           map.put("annokset", annokset.findAll()); 
           map.put("ainekset", ainekset.findAll());
          
           return new ModelAndView(map, "annokset");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/smoothiet", (req, res) -> {
            String nimi = req.queryParams("nimi");
            
            annokset.saveOrUpdate(new Annos(-1, nimi));
            
            res.redirect("/smoothiet");
            return "";
        });
        
        Spark.post("/smoothiet/aines", (req, res) -> {
            Integer annos = Integer.parseInt(req.queryParams("smoothie"));
            Integer aines = Integer.parseInt(req.queryParams("raakaAine"));
            Integer jarjestys = Integer.parseInt(req.queryParams("jarjestys"));
            String nimi = req.queryParams("maara");
            String ohje = req.queryParams("ohje");

            annoksetAinekset.saveOrUpdate(new AnnosRaakaAine(annokset.findOne(annos), ainekset.findOne(aines),jarjestys, nimi, ohje));

            res.redirect("/smoothiet");
            return "";
        });
        
        Spark.get("/smoothiet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annokset", annokset.findAll());
            map.put("ainekset", ainekset.findAll());

            return new ModelAndView(map, "annokset");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/smoothiet/:id/poista", (req, res) -> {
            Integer taskId = Integer.parseInt(req.params(":id"));
            
            annokset.delete(taskId);
            
            res.redirect("/ainekset");
            
            return "";
        });
        
        Spark.get("/smoothiet/:id", (req, res) -> {
            Integer id = Integer.parseInt(req.params(":id"));
            
            HashMap map = new HashMap<>();
            map.put("annos", annokset.findOne(id));
            map.put("ainekset", annoksetAinekset.findAll(id));
           

            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/tilastot", (req, res) -> {
            
            List<RaakaAine> aineet = ainekset.findAll();
            HashMap<RaakaAine, Integer> tilastot = new HashMap<>();
            
            HashMap map = new HashMap<>();
            
            aineet.forEach(x -> tilastot.put(x, annoksetAinekset.count(x.getId()) ));
            
            map.put("tilastot", tilastot);
            
            return new ModelAndView(map, "tilastot");
            
        }, new ThymeleafTemplateEngine());
        
    }

    
}
