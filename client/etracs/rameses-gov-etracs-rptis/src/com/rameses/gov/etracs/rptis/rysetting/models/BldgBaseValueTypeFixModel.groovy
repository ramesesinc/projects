package com.rameses.gov.etracs.rptis.rysetting.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*

public class BldgBaseValueTypeFixController extends BldgBaseValueTypeModel
{
    public Column[] getColumns() { 
        return [
            new Column(name:'bldgkind', caption:'Bldg Code*', editable:true, type:'lookup', expression:'#{item.bldgkind.code}', handler:'bldgkind:lookup', maxWidth:80),
            new Column(name:'bldgkind.name', caption:'Kind of Building' ),
            new Column(name:'basevalue', caption:'Base Value', type:'decimal', editable:true),
        ]
    }

}