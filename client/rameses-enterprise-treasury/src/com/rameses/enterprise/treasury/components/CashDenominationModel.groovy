package ccom.rameses.enterprise.treasury.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

class CashDenominationModel extends ComponentBean {
        
    @Binding
    def binding;
    
    def formatter;
    def model;
    
    int qty1000 = 0;
    int qty500 = 0;
    int qty200 = 0;
    int qty100 = 0;
    int qty50 = 0;
    int qty20 = 0;
    int qty10 = 0;
    int qty5 = 0;
    int qty1 = 0;
    
    double d1000 = 0.0;
    double d500 = 0.0;
    double d200 = 0.0;
    double d100 = 0.0;
    double d50 = 0.0;
    double d20 = 0.0;
    double d10 = 0.0;
    double d5 = 0.0;
    def coins = 0.0;
        
    def total = 0.0;
    def amount = 0.0;
    def cashremaining = 0.0;
    
    void calcTotals() {
        total = NumberUtil.round(d1000+d500+d200+d100+d50+d20+d10+d5+coins);
        cashremaining = NumberUtil.round(amount - total);
        binding.refresh("total|cashremaining");
    };

    void loadData( def denomination, def qtyFld, def amtFld ) {
        def d = model.find{ it.denomination == denomination };
        if(d) {
            this[(qtyFld)] = d.qty;
            this[(amtFld)] = d.amount;
        }
    };
    
    void init() {
        model = getValue();
        if(model) {
            loadData( 1000, 'qty1000', 'd1000');
            loadData( 500, 'qty500', 'd500');
            loadData( 200, 'qty200', 'd200');
            loadData( 100, 'qty100', 'd100');
            loadData( 50, 'qty50', 'd50');
            loadData( 20, 'qty20', 'd20');
            loadData( 10, 'qty10', 'd10');
            loadData( 5, 'qty5', 'd5'); 
            coins = model.find{it.denomination == 1}?.amount;
        };
        else {
            model = [];
            setValue(model);
        }
        calcTotals();
    };
    
    void reload() {
        calcTotals();
    }
    
    void updateEntry(def denomination, def qty,  def amtFld) {
        this[(amtFld)] = qty * denomination;
        def d = model.find{ it.denomination == denomination };
        if(d) {
            d.qty = qty;
            d.amount = this[(amtFld)];
        }
        else {
            model << [caption: denomination, denomination:denomination, qty: qty, amount: this[(amtFld)]];
        }
        calcTotals();
    }
    
    void updateCoinsAmount( def amt ) {
        def d = model.find{ it.denomination == 1 };
        if(d) {
            d.qty = amt;
            d.amount = amt;
        }
        else {
            model << [caption: 'Coins', denomination:1, qty: amt, amount: amt];
        }
        calcTotals();
    }
    
    @PropertyChangeListener 
    def listener = [
        "qty1000": {q-> updateEntry(1000, q, 'd1000'); },
        "qty500": {q-> updateEntry(500, q, 'd500'); },
        "qty200": {q-> updateEntry(200, q, 'd200');},
        "qty100": {q-> updateEntry(100, q, 'd100');},
        "qty50": {q-> updateEntry(50, q, 'd50');},
        "qty20": {q-> updateEntry(20, q, 'd20');},
        "qty10": {q-> updateEntry(10, q, 'd10'); },
        "qty5": {q-> updateEntry(5, q, 'd5');},
        "coins": {o-> updateCoinsAmount(o) }
    ];
    
}