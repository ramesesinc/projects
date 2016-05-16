class MunicipalityTestFaas_Stewardship_Approve
{
    public static void runTest(faas, stewardshipno){
        println '='*50
        println '[municipality] Stewardship Approval Test'
        println '='*50

        def provhelper = ProvinceTestProxy.create('RPTISTestHelperService')
        def munihelper = MunicipalityTestProxy.create('RPTISTestHelperService')

        println '================================================='
        println 'MUNICIPALITY: CREATE DATACAPTURE FAAS '
        println '================================================='
        println '[municipality] Capturing FAAS'
        def munisvc = MunicipalityTestProxy.create('RPTISTestFAASDataCaptureService')
        faas = munisvc.createDataCapture(faas)
        munisvc.submitDataCaptureForApproval(faas)
        println munisvc.testSubmittedDataCapture(faas)
        munisvc.approveDataCapture(faas)
        println munisvc.testApprovedDataCapture(faas)
        println '[municipality] Data Capture FAAS created'

        println '================================================'
        println 'MUNICIPALITY: APPROVE DATA CAPTURE LEDGER'
        println '================================================'
        munisvc = MunicipalityTestProxy.create('RPTISTestLedgerService')
        println munisvc.testPendingLedgerFromFaas(faas)
        def ledger = munisvc.approveLedgerFromFaas(faas, true)
        println munisvc.testApprovedLedger(ledger)



        println '================================================='
        println 'MUNICIPALITY: TEST STEWARDSHIP'
        println '================================================='
        println '[municipality] Processing Stewardship'
        munisvc = MunicipalityTestProxy.create('RPTISMunicipalityTestStewardshipService')
        def info = [faas:faas, stewardshipno:stewardshipno]
        def task = munisvc.initStewardship(info)
        def stfaas = [objid:task.objid]

        task = munisvc.doReceive(task)
        task = munisvc.doTaxmapping(task)
        task = munisvc.doAppraisal(task)
        task = munisvc.doRecommender(task)
        task = munisvc.doApprover(task)
        println munisvc.testApprovedStewardship(stfaas)
        println munisvc.testApprovedLedgerFromFaas(stfaas)
        println '[municipality] Simple Transfer approved.'


        println '=========================================================='
        println 'PROVINCE: TEST MUNICIPALITY APPROVED SIMPLE TRANSFER FAAS '
        println '=========================================================='
        def provsvc = ProvinceTestProxy.create('RPTISProvinceTestMunicipalityStewardshipService')
        TestHelper.waitForFaas(stfaas, provhelper)
        println provsvc.testMunicipalityApprovedFaasStewardship(stfaas)
        println provsvc.testMunicipalityApprovedFaasStewardshipLedgerByFaas(stfaas)
        println 'Test Completed.'
    }
}
