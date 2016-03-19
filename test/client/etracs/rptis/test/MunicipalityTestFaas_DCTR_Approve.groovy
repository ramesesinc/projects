class MunicipalityTestFaas_DCTR_Approve
{
    public static void runTest(faas){
        println '='*50
        println '[municipality] Data capture FAAS, Simple Transfer Test and Approval'
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



        /*=================================================
        * MUNICIPALITY: TEST SIMPLE TRANSFER FAAS 
        =================================================*/
        println '[municipality] Processing FAAS Simple Transfer'
        munisvc = MunicipalityTestProxy.create('RPTISMunicipalityTestTRFAASService')
        def task = munisvc.createSimpleTransferFaas(faas)
        def trfaas = [objid:task.objid]

        task = munisvc.doReceive(task)
        //task = munisvc.doExamination(task)
        task = munisvc.doTaxmapping(task)
        //task = munisvc.doTaxmappingApproval(task)
        task = munisvc.doAppraisal(task)
        //task = munisvc.doAppraisalApproval(task)
        task = munisvc.doRecommender(task)
        task = munisvc.doApprover(task)
        println munisvc.testApprovedTransfer(trfaas)
        println munisvc.testApprovedLedgerFromFaas(trfaas)
        println '[municipality] Simple Transfer approved.'


        /*=================================================
        * PROVINCE: TEST MUNICIPALITY APPROVED SIMPLE TRANSFER FAAS 
        =================================================*/
        def provsvc = ProvinceTestProxy.create('RPTISProvinceTestMunicipalityTxnApprovedService')
        TestHelper.waitForFaas(trfaas, provhelper)
        println provsvc.testMunicipalityApprovedFaasTxn(trfaas)
        println provsvc.testMunicipalityApprovedFaasTxnLedgerByFaas(trfaas)
        println 'Test Completed.'
    }
}
