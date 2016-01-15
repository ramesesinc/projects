package com.rameses.clfc.util;

class LoanUtil
{
    private static def appTypes;
    private static def clientTypes;    
    private static def kindTypes;
    private static def occupancyTypes;
    private static def rentTypes;
    private static def ownershipTypes;
    private static def vehicleKinds;
    private static def vehicleUses;
    private static def propertyClassificationTypes;
    private static def propertyUomTypes;
    private static def propertyModeOfAcquisitionTypes;
    private static def otherLendingModesOfPayment;
    private static def borrowerOccupancyTypes;
    private static def paymentTypes;
    private static def compromiseTypes;
    private static def denominations;

    public static def getAppTypes() {
        if (appTypes == null) {
            appTypes = [
                [key:'NEW', value:'NEW'], 
                [key:'RENEW', value:'RENEW']
            ]
        }
        return appTypes;
    }
    
    public static def getClientTypes() {
        if (clientTypes == null) {
            clientTypes = [
                [key:'WALK_IN', value:'WALK-IN'], 
                [key:'MARKETED', value:'MARKETED']
            ]
        }
        return clientTypes;
    }
    
    public static def getKindTypes() {
        if( kindTypes == null ) {
            kindTypes = [
                [key:'SARI_SARI', value:'SARI-SARI STORE'], 
                [key:'CARENDERIA', value:'CARENDERIA'], 
                [key:'OPERATOR', value:'OPERATOR'], 
                [key:'OTHERS', value:'OTHERS']
            ]
        }
        return kindTypes;
    }

    public static def getOccupancyTypes() {
        if( occupancyTypes == null ) {
            occupancyTypes = [
                [key:'OWNED', value:'OWNED'], 
                [key:'RENTED', value:'RENTED'], 
                [key:'FREE_USE', value:'FREE USE'], 
                [key:'OTHERS', value:'OTHERS']
            ]
        }
        return occupancyTypes;
    }

    public static def getRentTypes() {
        if( rentTypes == null ) {
            rentTypes = [
                [key:'W_CONTRACT', value:'W/ CONTRACT'], 
                [key:'WO_CONTRACT', value:'W/O CONTRACT']
            ]
        }
        return rentTypes;
    }

    public static def getOwnershipTypes() {
        if( ownershipTypes == null ) {
            ownershipTypes = [
                [key:'SOLE', value:'SOLE PROPRIETORSHIP'], 
                [key:'PARTNERSHIP', value:'PARTNERSHIP'], 
                [key:'CORPORATION', value:'CORPORATION']
            ]
        }
        return ownershipTypes;
    }
    
    public static def getVehicleKinds() {
        if( vehicleKinds == null ) {
            vehicleKinds = [
                [key:'MOTORCYCLE', value:'MOTORCYCLE'], 
                [key:'PUJ', value:'PUJ'], 
                [key:'OTHERS', value:'OTHERS']
            ]
        }
        return vehicleKinds;
    }
    
    public static def getVehicleUses() {
        if( vehicleUses == null ) {
            vehicleUses =  [
                [key:'FOR_HIRE', value:'FOR HIRE'], 
                [key:'PRIVATE', value:'PRIVATE']
            ]
        }
        return vehicleUses;
    }
    
    public static def getPropertyClassificationTypes() {
        if( propertyClassificationTypes == null ) {
            propertyClassificationTypes = [
                [key:'RESIDENCIAL', value:'RESIDENCIAL'], 
                [key:'AGRICULTURAL', value:'AGRICULTURAL']
            ]
        }
        return propertyClassificationTypes;
    }
    
    public static def getPropertyUomTypes() {
        if( propertyUomTypes == null ) {
            propertyUomTypes = [
                [key:'SQM', value:'sqm'], 
                [key:'HECTARES', value:'hectares']
            ]
        }
        return propertyUomTypes;
    }
    
    public static def getPropertyModeOfAcquisitionTypes() {
        if( propertyModeOfAcquisitionTypes == null ) {
            propertyModeOfAcquisitionTypes = [
                [key:'INHERETED', value:'INHERETED'], 
                [key:'SALE', value:'SALE'], 
                [key:'DONATION', value:'DONATION'], 
                [key:'GRANTS', value:'GRANTS/AWARDS'], 
                [key:'OTHERS', value:'OTHERS']
            ]
        }
        return propertyModeOfAcquisitionTypes;
    }
    
    public static def getOtherLendingModesOfPayment() {
        if( otherLendingModesOfPayment == null ) {
            otherLendingModesOfPayment = [
                [key:'DAILY', value:'DAILY'], 
                [key:'WEEKLY', value:'WEEKLY'], 
                [key:'MONTHLY', value:'MONTHLY']
            ]
        }
        return otherLendingModesOfPayment;
    }
    
    public static def getBorrowerOccupancyTypes() {
        if( borrowerOccupancyTypes == null ) {
            borrowerOccupancyTypes = [
                [key:'OWNED', value:'OWNED'],
                [key:'LIVING_WTIH', value:'LIVING WITH'],
                [key:'COMPANY_OWNED', value:'COMPANY OWNED'],
                [key:'RENTED', value:'RENTED'],
                [key:'FREE_USE', value:'FREE USE'],
                [key:'MORTGAGE_BANK', value:'MORTGAGE BANK'],
                [key:'RIGHTS', value:'RIGHTS'],
                [key:'SQUATTERS', value:'SQUATTERS']
            ]
        }
        return borrowerOccupancyTypes;
    }
    
    public static def getPaymentTypes() {
        if(paymentTypes == null) {
            paymentTypes = [
                [name:'Schedule/Regular', value:'schedule'],
                [name:'Overpayment', value:'over']
            ]
        }
        return paymentTypes;
    }

    public static def getCompromiseTypes() {
        if (compromiseTypes == null) {
            compromiseTypes = [
                [key:'FIXED', value:'FIXED'],
                [key:'WAIVE_INTEREST', value:'WAIVE INTEREST'],
                [key:'WAIVE_PENALTY', value:'WAIVE PENALTY']
            ]
        }
        return compromiseTypes;
    }

    public static def getDenominations() {
        if (denominations == null) {
            denominations = [
                [caption: 1000.00, denomination: 1000, qty: 0, amount: 0],
                [caption: 500.00, denomination: 500, qty: 0, amount: 0],
                [caption: 200.00, denomination: 200, qty: 0, amount: 0],
                [caption: 100.00, denomination: 100, qty: 0, amount: 0],
                [caption: 50.00, denomination: 50, qty: 0, amount: 0],
                [caption: 20.00, denomination: 20, qty: 0, amount: 0],
                [caption: 10.00, denomination: 10, qty: 0, amount: 0],
                [caption: 5.00, denomination: 5, qty: 0, amount: 0],
                [caption: 1.00, denomination: 1, qty: 0, amount: 0],
                [caption: 0.25, denomination: 0.25, qty: 0, amount: 0],
                [caption: 0.10, denomination: 0.10, qty: 0, amount: 0],
                [caption: 0.05, denomination: 0.05, qty: 0, amount: 0],
            ]
        }
        return denominations;
    }
}