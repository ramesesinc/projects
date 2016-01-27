package com.rameses.waterworks.calc;

import bsh.EvalError;
import bsh.Interpreter;
import com.rameses.Main;
import com.rameses.waterworks.bean.Formula;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;

/**
 *
 * @author Rameses
 */
public class BillCalculator {
    
    public double compute(String classificationid, int vol){
        double value = 0.00;
        try{
            Database db = DatabasePlatformFactory.getPlatform().getDatabase();
            Formula f = db.getFormula(classificationid);
            Main.LOG.error("EXPRESSION", f.getExpr());
            if(!db.getError().isEmpty()){
                Dialog.showAlert(db.getError());
            }
            if(f == null){
                Dialog.showAlert("Formula cannot be found!");
                return 0.00;
            }
            Interpreter i = new Interpreter();
            i.set("VOL", vol);
            Object o = i.eval(f.getExpr());
            value = Double.parseDouble(o.toString());
            Main.LOG.error("VALUE", String.valueOf(value));
        }catch(EvalError e){
            e.printStackTrace();
            Dialog.showAlert("ERROR: " +  e.toString());
        }
        return value;
    }
    
}
