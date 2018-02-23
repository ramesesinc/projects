package rptis.landtax.facts;

public class Bill
{
	Date currentdate 
	Date expirydate 
    Integer billtoyear
    Integer billtoqtr

    def entity 
    
    public Bill(){}

    public Bill(bill){
    	this.entity = bill 
    	this.currentdate = bill.currentdate 
        this.billtoyear = bill.billtoyear
        this.billtoqtr = bill.billtoqtr
    }

    public void setExpirydate(expirydate){
    	this.expirydate = expirydate
    	entity.expirydate = expirydate
    }
}
