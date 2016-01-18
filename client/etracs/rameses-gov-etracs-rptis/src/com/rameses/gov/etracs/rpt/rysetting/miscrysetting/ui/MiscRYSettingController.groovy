package com.rameses.gov.etracs.rpt.rysetting.miscrysetting.ui;
        
import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rpt.util.RPTUtil;
import com.rameses.gov.etracs.rpt.rysetting.ui.RYSettingPageFlowController;

public class MiscRYSettingController extends RYSettingPageFlowController
{
    @Service('MiscRYSettingService')
    def svc;
    
    
    @Service('RPTParameterService')
    def paramSvc 
            
    public def getService(){ return svc; }
    public String getSettingType(){ return 'misc'; }
    public String getSettingTitle(){return 'Miscellaneous Item Revision Setting'; }
    public String getPrefixId(){ return 'MRY'; }
    String entityName = 'miscrysetting';
    
    
    void loadItems() {
        assessLevels    = svc.getAssessLevels(entity.objid)
        miscItemValues  = svc.getMiscItemValues(entity.objid)
    }
    
    
    /*---------------------------------------------------------------------
    *
    * MiscItemValue Support
    *
    ---------------------------------------------------------------------*/
    def selectedMiscItem 
    def miscItemValues = []
    
    def _expr;
    
    def handle = { o-> 
        selectedMiscItem.expr = _expr;
        miscItemListHandler.refreshEditedCell()
        if ( ! selectedMiscItem.isnew)
            svc.saveMiscItemValue( selectedMiscItem );
    };
    
    def getLookupEditor() {
        _expr = selectedMiscItem.expr;
        def model = [
            getValue: {
                return _expr;
            },
            setValue: { o->
                _expr = o;
            },
            getVariables: { type->
                def vars = paramSvc.getVariableList();
                return vars.collect{
                    [caption: it.name, title:it.name,  signature: it.name, description : "("+it.paramtype +")" ]
                };
            }
        ] as ExpressionModel;
        
        return InvokerUtil.lookupOpener("expression:editor", [model:model, updateHandler:handle] );
    }


    
    def getLookupMiscItem(){
        return InvokerUtil.lookupOpener('miscitem:lookup',[
            onselect: { item ->
                selectedMiscItem.miscitem = item
            },
            onempty: {
                selectedMiscItem.miscitem = null
            }
        ])
    }
    
    def miscItemListHandler = [
        getRows    : { return miscItemValues.size() + 5 },
            
        createItem : { return [
            objid : RPTUtil.generateId('MI'),
            miscrysettingid : entity.objid,
            isnew : true,
        ]},
            
        getColumns : { return [
            new Column(caption:'Code', type:'lookup', handler:lookupMiscItem, editable:true, expression:'#{item.miscitem.code}',  maxWidth:80),
            new Column(name:'miscitem.name', caption:'Name'),
            new Column(name:"expr", editable:true, caption:"Computation Expression", typeHandler: new OpenerColumnHandler( handler: "lookupEditor" ) )
        ]},
                
        validate : { li -> 
            def miv = li.item
            required( miv.miscitem, 'Code')
            required( miv.expr, 'Computation Expression')
            checkDuplicate(miv)
            svc.saveMiscItemValue( miv )
        },
                
                                
        onAddItem : { item ->
            miscItemValues.add(item);
            item.isnew = false;
        },
                
        fetchList    : { return miscItemValues },
    ] as EditorListModel
                
    void checkDuplicate(miv){
            def data = miscItemValues.find{it.miscitem.objid == miv.miscitem.objid && it.objid != miv.objid }
            if (data)
                throw new Exception("Duplicate item is not allowed.")
    }
    
        
    
    /*---------------------------------------------------------------------
    *
    * Create Records Support
    *
    ---------------------------------------------------------------------*/
    Map createEntity() {
        return [
            objid       : RPTUtil.generateId('MRY'),
            ry          : null,
        ]
    }
        
    Map createAssessLevel() {
        return [ 
            objid           : RPTUtil.generateId('AL'),
            miscrysettingid : entity.objid,
            fixrate         : true,
            rate            : 0.0,
            ranges          : [],
        ]
    }
    
    Map createAssessLevelRange() {
        return [ 
            objid               : RPTUtil.generateId('ALR'),
            miscassesslevelid   : selectedAssessLevel.objid,
            miscrysettingid     : entity.objid,
            mvfrom  : null,
            mvto    : null,
            rate    : 0.0,
        ]
    }
}

