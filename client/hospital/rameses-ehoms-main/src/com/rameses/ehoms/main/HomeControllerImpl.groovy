package rameses.ehoms.main;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;

public class HomeControllerImpl 
{
    def model;
    def items;
    
    void init() { 
        items = [];
        items.addAll(Inv.lookupActions('home.action'));
        items.each { 
            if (!it.icon) it.icon = 'images/home-icon.png';
        } 
                
        model = [
            fetchList: {o-> 
                return items; 
            }, 
            
            onOpenItem: {o->             
                o.execute();
            }
        ] as TileViewModel
    } 
}
