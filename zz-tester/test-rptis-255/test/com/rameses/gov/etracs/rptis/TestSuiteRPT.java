
package com.rameses.gov.etracs.rptis;

import test.rptis.FaasDataCaptureApproveTest;
import test.rptis.FaasDataCaptureLandTest;
import test.entity.EntityTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    EntityTest.class,
    FaasDataCaptureLandTest.class,
    FaasDataCaptureApproveTest.class,
    FaasRevertToInterimTest.class,
    FaasTransferOnlineReceiveRedFlagTest.class,
    
    FaasTransferManualTest.class,
    FaasTransactionWithUnpaidLedgerTest.class,
    FaasTransferOnlineInitTest.class,
    FaasTransferOnlineReceiveTest.class,
    FaasTransactionWithTaxDifferenceTest.class,
    
    CancelledFaasManualTest.class,
    CancelledFaasOnlineReceiverTest.class,
    CancelledFaasOnlineTaxmapperTest.class,
    CancelledFaasOnlineRecommenderTest.class,
    CancelledFaasOnlineApproverTest.class,
    
    AnnotationCreateTest.class, 
    AnnotationSubmitTest.class,
    AnnotationApproveTest.class,
    
    CancelAnnotationCreateTest.class, 
    CancelAnnotationSubmitTest.class,
    CancelAnnotationApproveTest.class,
    
    FaasChangeOwnerInfoTest.class, 
    FaasRestrictionPaidTest.class,
    
    SubdivisionForApprovalTest.class,
    SubdivisionApprovalTest.class,
})
public class TestSuiteRPT {
    
}
