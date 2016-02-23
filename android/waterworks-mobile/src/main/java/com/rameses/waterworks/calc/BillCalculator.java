package com.rameses.waterworks.calc;

import bsh.EvalError;
import bsh.Interpreter;
import com.rameses.Main;
import com.rameses.util.ObjectDeserializer;
import com.rameses.waterworks.bean.Rule;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Rameses
 */
public class BillCalculator {
    
    private String error = "";
    
    public double compute(String info, int vol){
        double value = 0.00;
        boolean found = false;
        Map map;
        Interpreter interpreter;
        try{
            map = (Map) ObjectDeserializer.getInstance().read(info);
            interpreter = new Interpreter();
            for(Object o : map.entrySet()){
                Map.Entry me = (Map.Entry) o;
                interpreter.set(me.getKey().toString(), me.getValue().toString());
                Main.LOG.error("Interpreter Data",me.getKey().toString()+" = "+me.getValue().toString());
            }
            
            Rule rule = null;
            Iterator<Rule> i = Main.RULES.iterator();
            while(i.hasNext()){
                rule = i.next();
                Main.LOG.error("Condition", rule.getCondition());
                boolean test = (boolean) interpreter.eval(rule.getCondition());
                if(test) break;
                rule = null;
            }

            if(rule == null){
                error = "Rule cannot be found!";
                return 0.00;
            }
            interpreter.set(rule.getVar(), vol);
            Number num = (Number) interpreter.eval(rule.getAction());
            return num.doubleValue();
        }catch(EvalError e){
            e.printStackTrace();
            error = "ERROR: " +  e.toString();
        }

        return value;
    }
    
    public String getError(){
        return error;
    }
    
}
