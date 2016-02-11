package com.rameses.waterworks.bean;

public class Formula {
    
    private String classificationid, var, expr;
    
    public Formula(String classificationid, String var, String expr){
        this.classificationid = classificationid;
        this.var = var;
        this.expr = expr;
    }
    
    public String getClassificationId(){ return classificationid; }
    public String getVar(){ return var; }
    public String getExpr(){ return expr; }
    
}
