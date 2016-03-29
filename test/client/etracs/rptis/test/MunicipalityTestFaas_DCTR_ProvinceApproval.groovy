class MunicipalityTestFaas_DCTR_ProvinceApproval
{
    public static void runTest(faas){
        println '='*50
        println '[municipality] Data capture FAAS, Simple Transfer Test and Province Approval'
        println '='*50

        def provhelper = ProvinceTestProxy.create('RPTISTestHelperService')
        def munihelper = MunicipalityTestProxy.create('RPTISTestHelperService')

        /*=================================================
        * MUNICIPALITY: CREATE DATACAPTURE FAAS 
        =================================================*/
        println '[municipality] Capturing FAAS'
        def munisvc = MunicipalityTestProxy.create('RPTISTestFAASDataCaptureService')
        faas = munisvc.createDataCapture(faas)
        munisvc.submitDataCaptureForApproval(faas)
        println munisvc.testSubmittedDataCapture(faas)
        munisvc.approveDataCapture(faas)
        println munisvc.testApprovedDataCapture(faas)
        println '[municipality] Data Capture FAAS created'

        /*=================================================
        * MUNICIPALITY: APPROVE DATA CAPTURE LEDGER 
        =================================================*/
        munisvc = MunicipalityTestProxy.create('RPTISTestLedgerService')
        println munisvc.testPendingLedgerFromFaas(faas)
        def ledger = munisvc.approveLedgerFromFaas(faas, true)
        println munisvc.testApprovedLedger(ledger)
        println '[municipality] Approved ledger tested'



        /*=================================================
        * MUNICIPALITY: TEST SIMPLE TRANSFER FAAS 
        =================================================*/
        println '[municipality] Processing FAAS Simple Transfer'
        munisvc = MunicipalityTestProxy.create('RPTISMunicipalityTestTRFAASService')
        def task = munisvc.createSimpleTransferFaas(faas)
        def trfaas = [objid:task.objid]

        task = munisvc.doReceive(task)
        task = munisvc.doTaxmapping(task)
        task = munisvc.doAppraisal(task)
        task = munisvc.doRecommenderSubmitToProvince(task)
        munisvc.testSubmittedToProvinceOnlineFaas(trfaas)
        println '[municipality] Simple Transfer submitted to province for approval'


        /*=================================================
        * PROVINCE: APPROVE AND TEST TRANSFERRED FAAS FROM MUNICIPALITY 
        =================================================*/
        println '[province] Approve and test FAAS Transfer submitted to Province'
        TestHelper.waitForFaas(trfaas, provhelper)
        def provsvc = ProvinceTestProxy.create('RPTISProvinceTestMunicipalityTRProvinceApprovalService')
        task = provsvc.openFaas(trfaas)
        task = provsvc.doReceive(task)
        task = provsvc.doTaxmapping(task)
        task = provsvc.doTaxmappingApproval(task)
        task = provsvc.doAppraisal(task)
        task = provsvc.doAppraisalApproval(task)
        task = provsvc.doRecommender(task)
        task = provsvc.doApprover(task)
        provsvc.testApprovedTransfer(trfaas)
        println '[province] Transfer FAAS approved and tested'


        /*=================================================
        * PROVINCE: TEST MUNICIPALITY APPROVED SIMPLE TRANSFER FAAS 
        =================================================*/
        munisvc = MunicipalityTestProxy.create('RPTISMunicipalityTestTRFAASService')
        TestHelper.waitForCurrentFaas(trfaas, munihelper)
        println munisvc.testApprovedTransfer(trfaas)
        println munisvc.testApprovedLedgerFromFaas(trfaas)
        println 'Test Completed.'
    }
}
