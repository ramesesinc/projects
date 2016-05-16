class ProvinceTestFaas_DC_Cancel
{
    public static void runTest(faas){
        println '***************************************'
        println 'PROVINCE FAAS CANCELLATION TEST'
        println '***************************************'

        def provhelper = ProvinceTestProxy.create('RPTISTestHelperService')
        def munihelper = MunicipalityTestProxy.create('RPTISTestHelperService')

        '================================================='
        'PROVINCE: DATACAPTURE FAAS AND TEST '
        '================================================='
        def provsvc = ProvinceTestProxy.create('RPTISProvinceTestDCFAASService')
        faas = provsvc.createDataCapture(faas)
        provsvc.submitDataCaptureForApproval(faas)
        println provsvc.testSubmittedDataCapture(faas)
        provsvc.approveDataCapture(faas)
        println provsvc.testApprovedDataCapture(faas)


        '================================================='
        'MUNICIPALITY: APPROVE LEDGER AND TEST'
        '================================================='
        def munisvc = MunicipalityTestProxy.create('RPTISMunicipalityTestProvinceDCFAASService')
        TestHelper.waitForFaas(faas, munihelper)
        println munisvc.testProvinceCreatedDataCapture(faas)
        munisvc.approveLedger(faas)
        println munisvc.testApprovedLedger(faas)


        '================================================='
        'PROVINCE: CANCEL FAAS AND TEST '
        '==============================================='
        provsvc = ProvinceTestProxy.create('RPTISProvinceTestFAASCancelService')
        def task = provsvc.initCancelFaas(faas)
        def cancelledfaas = [objid:task.objid]
        task = provsvc.doReceive(task)
        task = provsvc.doTaxmapping(task)
        task = provsvc.doTaxmappingApproval(task)
        task = provsvc.doRecommender(task)
        task = provsvc.doApprover(task)
        println provsvc.testApprovedCancelledFaas(cancelledfaas)


        '================================================='
        'MUNICIPALITY: TEST PROVINCE CANCELLED FAAS'
        '==============================================='
        munisvc = MunicipalityTestProxy.create('RPTISMunicipalityTestCancelFAASService')
        TestHelper.waitForCancelledFaas(cancelledfaas, munihelper)
        println munisvc.testApprovedCancelledFaas(cancelledfaas)
        println 'Test Completed.'
    }
}
