package com.rameses.waterworks.calc;

import bsh.EvalError;
import bsh.Interpreter;
import com.rameses.waterworks.bean.Formula;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;

/**
 *
 * @author Rameses
 */
public class BillCalculator {
    
    private String error = "";
    
    public double compute(String classificationid, int vol){
        double value = 0.00;
        try{
            Database db = DatabasePlatformFactory.getPlatform().getDatabase();
            Formula f = db.getFormula(classificationid);
            error = db.getError();
            
            if(f == null){
                error = "Formula cannot be found!";
                return 0.00;
            }
            
            Interpreter i = new Interpreter();
            i.set("VOL", vol);
            Object o = i.eval(f.getExpr());
            value = Double.parseDouble(o.toString());
        }catch(EvalError e){
            error = "ERROR: " +  e.toString();
        }
        return value;
    }
    
    public String getError(){
        return error;
    }
    
}
