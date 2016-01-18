/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.lgu.aklan.terminal

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;

/**
 *
 * @author wflores 
 */
class AklanTurnstileController extends CRUDController {

    String serviceName = 'AklanTurnstileService';
    String entityName = 'aklanturnstile';
    String title = 'Turnstile Information';
    
    String createFocusComponent = 'entity.objid';
    String editFocusComponent = 'entity.title';
    boolean allowApprove = false;
    
    def selectedCategory;    
    def listhandler = [
        fetchList: { 
            return entity.categories; 
        } 
    ] as ListPaneModel;

    void removedSelectedCategory() {
        def item = entity.categories?.find{ it.objid == selectedCategory.objid }
        if (item != null) entity.categories.remove( item ); 
        
        listhandler.reload();
    }
    
    void afterOpen( o ) {
        binding?.refresh('lkpcategory');
        listhandler.reload(); 
    }
    void afterCreate( o ) {
        selectedCategory = null; 
        o.objid = null; 
        binding.focus('entity.objid'); 
        binding?.refresh('lkpcategory');
        listhandler.reload(); 
    }
    void afterSave( o ) {
        binding?.refresh('lkpcategory');
        listhandler.reload(); 
    }
    
    def getLookupCategory() {
        def params = [:];
        params.onselect = {o-> 
            def newlist = []; 
            entity.categories?.each{oo-> 
                if (oo.objid != o.objid) {
                    newlist << oo; 
                }
            }
            newlist << o;
            entity.categories.clear();
            entity.categories.addAll(newlist);
            listhandler.reload();
        }
        return Inv.lookupOpener('aklanturnstilecategory:lookup', params); 
    } 
    
}