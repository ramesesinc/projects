package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*
import java.rmi.server.UID;

public class CapturePaymentModel  
{
    @Caller
    def caller;
    
    @Binding
    def binding;
    
    @Service('RPTLedgerService')
    def svc;

    def entity 
    def payment
    
    String title = 'Ledger Payment'
    
    @PropertyChangeListener
    def listener = [
        'payment.basic'     : { copyTaxAmount('basic', 'sef') },
        'payment.basicint'  : { copyTaxAmount('basicint', 'sefint') },
        'payment.basicdisc' : { copyTaxAmount('basicdisc', 'sefdisc') },
    ]
    
    void copyTaxAmount(from, to){
        if (payment."$to" == 0.0){
            payment."$to" = payment."$from"
            binding.refresh("payment.$to")
        }
        calcTotal()
    }
    
    void create() {
        def fromyear = ( entity.lastqtrpaid == 4 ? entity.lastyearpaid + 1 : entity.lastyearpaid )
        def fromqtr  = ( entity.lastqtrpaid == 4 ? 1 : entity.lastqtrpaid + 1 )
        payment = [
            objid       : 'RP' + new UID(),
            type        : 'capture',
            receiptid   : null, 
            refid       : entity.objid,
            reftype     : 'rptledger',
            fromyear    : fromyear, 
            fromqtr     : fromqtr, 
            toyear      : fromyear,
            toqtr       : 4,
            basic       : 0.0,
            basicint    : 0.0,
            basicdisc   : 0.0,
            basicpartial: 0.0,
            basicidle   : 0.0,
            sef         : 0.0,
            sefint      : 0.0,
            sefdisc     : 0.0,
            sefpartial  : 0.0,
            firecode    : 0.0,
            amount      : 0.0,
            paidby      : [:],
        ]
    }
    
    def ok() {
        if( payment.toyear < payment.fromyear ) throw new Exception('To Year must be greater than or equal to from year.') 
        if( payment.toyear == payment.fromyear && payment.toqtr < payment.fromqtr ) throw new Exception('To Qtr must be greater than or equal to from qtr.') 
        checkNegative( 'Basic', payment.basic )
        checkNegative( 'Basic Discount', payment.basicdisc )
        checkNegative( 'Basic Penalty', payment.basicint )
        checkNegative( 'Idle Land', payment.basicidle )
        checkNegative( 'SEF', payment.sef )
        checkNegative( 'SEF Discount', payment.sefdisc )
        checkNegative( 'SEF Penalty', payment.sefint )
        checkNegative( 'Fire Code', payment.firecode )
        if( payment.basicdisc > payment.basic ) throw new Exception('Basic Discount must be less than Basic.')
        if( payment.sefdisc > payment.sef ) throw new Exception('SEF Discount must be less than SEF.')
        
        calcTotal()
        if( MsgBox.confirm('Posted payment could no longer be edited.\nPlease verify the information is correct.\n\nPost?') ) {
            svc.postPayment(payment)
            entity.lastyearpaid = payment.toyear;
            entity.lastqtrpaid  = payment.toqtr;
            caller.reloadEntity();
            caller.refreshSections();
            return '_close' 
        }
        return 'default' 
    }
    
    void calcTotal(){
        payment.amount = payment.basic + payment.basicint - payment.basicdisc +
                        payment.sef + payment.sefint - payment.sefdisc + payment.firecode
        binding.refresh('payment.amount')
    }
    

    List getQuarters() {
        return [1,2,3,4]
    }
    
    void checkNegative( caption, value ) {
        if( value == null ) throw new Exception( caption + ' must not be null.')
        if( value < 0.0 ) throw new Exception( 'Negative value for ' + caption + ' is not allowed.')
    }
}