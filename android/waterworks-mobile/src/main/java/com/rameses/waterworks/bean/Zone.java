/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.waterworks.bean;

/**
 *
 * @author Rameses
 */
public class Zone {
    
    private String id, code, desc, sector;
    
    public Zone(String id, String code, String desc, String sector){
        this.id = id;
        this.code = code;
        this.desc = desc;
        this.sector = sector;
    }
    
    public String getObjid(){ return id; }
    public String getCode(){ return code; }
    public String getDesc(){ return desc; }
    public String getSector(){ return sector; }
    
}
