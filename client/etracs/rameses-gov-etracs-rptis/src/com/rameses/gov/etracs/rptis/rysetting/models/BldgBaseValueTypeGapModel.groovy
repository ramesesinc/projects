package com.rameses.gov.etracs.rptis.rysetting.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*

public class BldgBaseValueTypeGapModel extends BldgBaseValueTypeModel
{
    public Column[] getColumns(){ 
        return [
            new Column(name:'bldgkind', caption:'Bldg Code*', editable:true, type:'lookup', expression:'#{item.bldgkind.code}', handler:'bldgkind:lookup', maxWidth:80),
            new Column(name:'bldgkind.name', caption:'Kind of Building' ),
            new Column(name:'minarea', caption:'Min Area*', type:'decimal', editable:true),
            new Column(name:'maxarea', caption:'Max Area*', type:'decimal', editable:true),
            new Column(name:'gapvalue', caption:'Gap*', type:'integer', editable:true),
            new Column(name:'minbasevalue', caption:'Min Base Value', type:'decimal', editable:true),
            new Column(name:'maxbasevalue', caption:'Max Base Value', type:'decimal', editable:true ),
        ]
    }

    public void validateItem( item ) {       
        if( item.minarea == null ) throw new Exception('Minimum Area is required.')
        if( item.maxarea == null ) throw new Exception('Maximum Area is required.')
        if( item.maxarea < item.minarea ) throw new Exception('Maximum Area must be greater than or equal to Minimum Area.')

        if( item.gapvalue == null ) throw new Exception('Gap is required.')
        if( item.gapvalue == 0 ) throw new Exception('Gap must be greater than zero.')

        if( item.minbasevalue == null ) throw new Exception('Minimum Base Value is required.')
        if( item.maxbasevalue == null ) throw new Exception('Maximum Base Value is required.')
        if( item.maxbasevalue < item.minbasevalue ) throw new Exception('Maximum Base Value must be greater than or equal to Minimum Base Value.')
    }
    

}