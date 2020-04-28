package com.rameses.gov.etracs.rptis.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

class RPTUtil
{
    public static def format( pattern, value ) {
        if( ! value ) value = 0
		def df = new DecimalFormat( pattern )
		return df.format( value )
	}
    
    public static def sum( list, field  ) {
        def total = list."$field".sum()
        if (total == null) total = 0.0
        return total
    }

    
    public static def generateId(prefix){
        return prefix + new java.rmi.server.UID();
    }
    
    public static void required( caption, value ) {
        def msg = caption + ' is required.'
        if ( value == null ) throw new Exception(msg)
        if ( !(value instanceof Number)  && !value) throw new Exception( msg )
    }
    
    public static boolean isTrue(value){
        return toBoolean(value, false) == true
    }
    
    public static boolean toBoolean(value, defvalue){
        if (value == null) 
        return defvalue;

        if (value instanceof Boolean) 
        return value;

        if ('1/y/yes/t/true'.indexOf(value.toString()) >= 0 ) 
        return true;
        
        return false;
    }
    
    public static def toDecimal(value){
        if (value instanceof BigDecimal ) 
        return value;

        def val = 0;
        try {
            val = new BigDecimal(value.toString());
        } catch( e) {
            e.printStackTrace();
        }
        return val;
    }
    
    public static def toInteger(value){
        if (value instanceof Integer) {
            return value;
        }

        def val = 0;
        try {
            def bd = new BigDecimal(value.toString());
            val = bd.intValue();
        } catch (e) {
            e.printStackTrace();
        }
        return val;
    }
    
     
    public static void checkDuplicate( list, caption, field, objid, value ) {
        def data = list.find{ it.objid != objid && it[field] == value }
        if( data ) throw new Exception('Duplicate ' + caption + ' is not allowed.')
    } 
    
    
    public static void buildPin(entity, varSvc){       
        def newpin = new StringBuilder();
        def provcity = entity.barangay?.provcity;
        def munidistrict = entity.barangay?.munidistrict;

        if( entity.barangay && entity.barangay.oldindexno == null) {
            entity.barangay.oldindexno = entity.barangay.indexno ;
            entity.barangay.oldpin  = entity.barangay.pin ;
        }
        
        if( entity.barangay && entity.pintype == 'new') {
            newpin += entity.barangay?.pin + '-';
        }
        else if( entity.barangay && entity.pintype == 'old') {
            entity.useoldpin = true;
            if (entity.useoldpin){
                newpin += entity.barangay?.oldpin + '-';
            }
            else {
                newpin += entity.barangay?.pin + '-';
            }
        }
        else {
            newpin += ( entity.pintype == 'new' ? '000-00-0000' : '000-00-000') + '-';
        }        
        
        def ssection = '';
        def sparcel = '';

        def sectionlen = getSectionLength(varSvc?.get('pin_section_length'), entity.pintype)
        def parcellen = getParcelLength(varSvc?.get('pin_parcel_length'), entity.pintype)
        
        if( entity.isection > 0 ) {
            ssection = entity.isection.toString();
            ssection = ssection.padLeft(sectionlen,'0');
            entity.section = ssection;
            newpin += ssection + '-';
        }
        else {
            ssection = ssection.padLeft(sectionlen,'0');
            entity.section = ssection;
            newpin += ssection + '-';
        }

        
        if( entity.iparcel > 0 ) {
            sparcel = entity.iparcel.toString();
            sparcel = sparcel.padLeft(parcellen,'0')
            entity.parcel = sparcel;
            newpin += sparcel;
        }
        else {
            sparcel = sparcel.padLeft(parcellen,'0')
            entity.parcel = sparcel;
            newpin += sparcel ;
        }
        
        if (entity.rputype != 'land'){
            if (entity.suffix){
                newpin += '-' + entity.suffix;
                if (entity.subsuffix) {
                    newpin += '-' + entity.subsuffix;
                }
            }
        }

        if (entity.claimno){
            newpin += '-' + entity.claimno
        }
        
        entity.pin= newpin;
        entity.fullpin = newpin;

    }
    
    static int getParcelLength(parcellen, pintype){
        int len = 3; //default old 
        try{
            len = new java.math.BigDecimal(parcellen).intValue();
        }
        catch(e){
            if (pintype.equalsIgnoreCase('new'))
            len = 2;
        }
        return len;
    }
    
    
        
    static int getSectionLength(sectionlen, pintype){
        int len = 2; //default old 
        try{
            len = new java.math.BigDecimal(sectionlen).intValue();
        }
        catch(e){
            if (pintype.equalsIgnoreCase('new'))
            len = 3;
        }
        return len;
    }

    public static def formalizeNumber( num ) {
        def snum = format('#0', num )
        if (snum.matches('11|12|13')) return snum + 'TH'
        else if (snum[-1] == '1') return snum + 'ST'
        else if (snum[-1] == '2') return snum + 'ND'
        else if (snum[-1] == '3') return snum + 'RD'
        else return snum + 'TH'
    }
}
