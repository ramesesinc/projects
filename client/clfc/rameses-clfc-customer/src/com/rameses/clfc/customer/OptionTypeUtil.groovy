package com.rameses.clfc.customer;

class OptionTypeUtil
{
    private static def genderTypes;    
    private static def civilStatusTypes;

    public static def getGenderTypes() {
        if (genderTypes == null) {
            genderTypes = [
                [key:'M', value:'MALE'], 
                [key:'F', value:'FEMALE']
            ]; 
        }
        return genderTypes;
    }
    
    public static def getCivilStatusTypes() {
        if (civilStatusTypes == null) {
            civilStatusTypes = [
                [key:'SINGLE', value:'SINGLE'], 
                [key:'MARRIED', value:'MARRIED'], 
                [key:'COUPLE', value:'COUPLE'], 
                [key:'WIDOW', value:'WIDOW/WIDOWER'], 
                [key:'SEPARATED', value:'SEPARATED']
            ]; 
        } 
        return civilStatusTypes;
    } 
}
