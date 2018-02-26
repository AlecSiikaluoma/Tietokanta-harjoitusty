package com.company.tikapeharjoitustyo2.domain;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alecsiikaluoma
 */
public class RaakaAine {
    
    private Integer id;
    private String nimi;
    
    public RaakaAine(Integer id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }
    
    public String getNimi() {
        return this.nimi;
    }
    
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
}
