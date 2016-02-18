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
public class Rule {
    
    private int salience;
    private String condition, var, action;
    
    public Rule(int salience, String condition, String var, String action){
        this.salience = salience;
        this.condition = condition;
        this.var = var;
        this.action = action;
    }
    
    public int getSalience(){ return salience; }
    public String getCondition(){ return condition; }
    public String getVar(){ return var; }
    public String getAction(){ return action; }
    
}
