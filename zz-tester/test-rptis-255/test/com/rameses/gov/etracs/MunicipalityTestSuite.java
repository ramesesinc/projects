
package com.rameses.gov.etracs;

import com.rameses.gov.etracs.landtax.RPTLedgerFixTest;
import com.rameses.gov.etracs.landtax.RPTLedgerIncentiveTest;
import com.rameses.gov.etracs.landtax.RPTLedgerTest;
import test.rptis.FaasDataCaptureApproveTest;
import test.rptis.FaasDataCaptureLandTest;
import test.entity.EntityTest;
import com.rameses.gov.etracs.rptis.FaasTransferManualTest;
import com.rameses.gov.etracs.rptis.FaasTransactionWithUnpaidLedgerTest;
import com.rameses.gov.etracs.rptis.FaasTransferOnlineInitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    EntityTest.class,
    FaasDataCaptureLandTest.class,
    FaasDataCaptureApproveTest.class,
    FaasTransactionWithUnpaidLedgerTest.class,
    FaasTransferManualTest.class,
    FaasTransferOnlineInitTest.class,
    
    RPTLedgerTest.class,
    RPTLedgerFixTest.class,
    RPTLedgerIncentiveTest.class
})
public class MunicipalityTestSuite {
    
}
