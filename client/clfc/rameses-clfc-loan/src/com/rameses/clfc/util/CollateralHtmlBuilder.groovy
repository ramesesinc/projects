package com.rameses.clfc.util;

class CollateralHtmlBuilder extends HtmlBuilder
{
    private StringBuffer sb;
    
    public def buildAppliance( appliance ) {
        if (appliance == null) return '';
        sb=new StringBuffer();
        sb.append(template.getResult(url+'collateral_appliance.gtpl', [appliance: appliance, ifNull: ifnull]));
        return getHtml(sb.toString());
    }
    
    public def buildProperty( property ) {
        if (property == null) return '';
        sb=new StringBuffer();
        sb.append(template.getResult(url+'collateral_property.gtpl', [property: property, ifNull: ifnull]));
        return getHtml(sb.toString());
    }
    
    public def buildVehicle( vehicle ) {
        if (vehicle == null) return '';
        sb=new StringBuffer();
        sb.append(template.getResult(url+'collateral_vehicle.gtpl', [vehicle:vehicle, ifNull: ifnull]));
        return getHtml(sb.toString());
    }
}