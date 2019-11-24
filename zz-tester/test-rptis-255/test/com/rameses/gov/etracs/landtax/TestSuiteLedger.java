/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.landtax;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    RPTLedgerTest.class,
    RPTLedgerFixTest.class,
    RPTLedgerIncentiveTest.class
})
public class TestSuiteLedger {
    
}
