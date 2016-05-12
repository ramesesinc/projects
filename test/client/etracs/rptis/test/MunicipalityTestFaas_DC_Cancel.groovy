class MunicipalityTestFaas_DC_Cancel
{
    public static void runTest(faas){
        println '*************************************************'
        println 'DATA CAPTURE AND CANCEL FAAS TEST'
        println '*************************************************'

        def provhelper = ProvinceTestProxy.create('RPTISTestHelperService')
        def munihelper = MunicipalityTestProxy.create('RPTISTestHelperService')

        println '================================================='
        println 'MUNICIPALITY: CREATE DATACAPTURE FAAS '
        println '================================================='
        def munisvc = MunicipalityTestProxy.create('RPTISTestFAASDataCaptureService')
        faas = munisvc.createDataCapture(faas)
        munisvc.submitDataCaptureForApproval(faas)
        println munisvc.testSubmittedDataCapture(faas)
        munisvc.approveDataCapture(faas)
        println munisvc.testApprovedDataCapture(faas)


        println '================================================='
        println 'MUNICIPALITY: APPROVE DATA CAPTURE LEDGER '
        println '================================================='
        munisvc = MunicipalityTestProxy.create('RPTISTestLedgerService')
        println munisvc.testPendingLedgerFromFaas(faas)
        def ledger = munisvc.approveLedgerFromFaas(faas, true)
        println munisvc.testApprovedLedger(ledger)
        TestHelper.waitForFaas(faas, provhelper)


        println '================================================='
        println 'MUNICIPALITY: CREATE CANCELLATION TRANSACTION'
        println '================================================='
        munisvc = MunicipalityTestProxy.create('RPTISMunicipalityTestCancelFAASService')
        def task = munisvc.initCancelFaas(faas)
        def cancelledfaas = [objid:task.objid]

        task = munisvc.doReceive(task)
        task = munisvc.doTaxmapping(task)
        task = munisvc.doRecommenderSubmitToProvince(task)


        println '*************************************************'
        println 'PROVINCE: APPROVED SUBMITTED CANCELLED FAAS'
        println '*************************************************'
        def provsvc = ProvinceTestProxy.create('RPTISProvinceTestMunicipalityCancelFaasApproval')
        TestHelper.waitForCancelledFaas(cancelledfaas, provhelper)

        task = provsvc.openCancelledFaas(cancelledfaas)
        task = provsvc.doReceive(task)
        task = provsvc.doTaxmapping(task)
        task = provsvc.doTaxmappingApproval(task)
        task = provsvc.doRecommender(task)
        task = provsvc.doApprover(task)
        println provsvc.testApprovedCancelledFaas(cancelledfaas)

        println '================================================='
        println 'MUNICIPALITY: TEST APPROVED CANCELLED FAAS'
        println '================================================='
        TestHelper.waitForApprovedCancelledFaas(cancelledfaas, munihelper)
        println munisvc.testApprovedCancelledFaas(cancelledfaas)
        println 'Test Completed.'

    }
}
