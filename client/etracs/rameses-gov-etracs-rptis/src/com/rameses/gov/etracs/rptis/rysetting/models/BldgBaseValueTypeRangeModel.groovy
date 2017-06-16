package com.rameses.gov.etracs.rptis.rysetting.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*

public class BldgBaseValueTypeRangeModel extends BldgBaseValueTypeModel
{
    public Column[] getColumns(){ 
        return [
            new Column(name:'bldgkind', caption:'Bldg Code*', editable:true, type:'lookup', expression:'#{item.bldgkind.code}', handler:'bldgkind:lookup', maxWidth:80),
            new Column(name:'bldgkind.name', caption:'Kind of Building' ),
            new Column(name:'minbasevalue', caption:'Minimum Base Value*', type:'decimal', editable:true ),
            new Column(name:'maxbasevalue', caption:'Maximum Base Value*', type:'decimal', editable:true),
        ]
    }
     
    void validateItem( item ) {       
        if( item.minbasevalue == null ) throw new Exception('Minimum Base Value is required.')
        if( item.maxbasevalue == null ) throw new Exception('Maximum Base Value is required.')
        if( item.maxbasevalue < item.minbasevalue ) throw new Exception('Maximum Base Value must be greater than or equal to Minimum Base Value.')
    }

}