class ProvinceTestFaas_DCTR_Approve
{
    public static void runTest(faas){
        println '='*50
        println '[province] Data capture FAAS and Simple Transfer Test'
        println '='*50

        def provhelper = ProvinceTestProxy.create('RPTISTestHelperService')
        def munihelper = MunicipalityTestProxy.create('RPTISTestHelperService')

        /*=================================================
        * PROVINCE: CREATE DATACAPTURE FAAS 
        =================================================*/
        println '[province] Capturing FAAS'
        def provsvc = ProvinceTestProxy.create('RPTISProvinceTestDCFAASService')
        faas = provsvc.createDataCapture(faas)
        provsvc.submitDataCaptureForApproval(faas)
        println provsvc.testSubmittedDataCapture(faas)
        provsvc.approveDataCapture(faas)
        println provsvc.testApprovedDataCapture(faas)
        println '[province] Data Capture FAAS created'


        /*=================================================
        * MUNICIPALITY: TEST FAAS AND APPROVE LEDGER 
        =================================================*/
        def munisvc = MunicipalityTestProxy.create('RPTISMunicipalityTestProvinceDCFAASService')
        TestHelper.waitForFaas(faas, munihelper)
        println munisvc.testProvinceCreatedDataCapture(faas)
        munisvc.approveLedger(faas)
        println munisvc.testApprovedLedger(faas)



        /*=================================================
        * PROVINCE: SIMPLE TRANSFER FAAS 
        =================================================*/
        println '[province] Processing FAAS Simple Transfer'
        provsvc = ProvinceTestProxy.create('RPTISProvinceTestTRFAASService')
        // def task = provhelper.getCurrentTaskFromPrevFaas(faas)

        provsvc.postProvinceLedger(faas)
        def task = provsvc.createSimpleTransferFaas(faas)
        def trfaas = [objid:task.objid]

        task = provsvc.doReceive(task)
        task = provsvc.doExamination(task)
        task = provsvc.doTaxmapping(task)
        task = provsvc.doTaxmappingApproval(task)
        task = provsvc.doAppraisal(task)
        task = provsvc.doAppraisalApproval(task)
        task = provsvc.doRecommender(task)
        task = provsvc.doApprover(task)
        println '[province] Simple Transfer approved.'


        /*=================================================
        * MUNICIPALITY: TEST SIMPLE TRANSFER FAAS 
        =================================================*/
        munisvc = MunicipalityTestProxy.create('RPTISMunicipalityTestProvinceDCTRApproveService')
        TestHelper.waitForFaas(trfaas, munihelper)
        println munisvc.testProvinceTransferredFaas(trfaas)
        println munisvc.testProvinceTransferredLedger(trfaas)
        println '='*50 
    }
}
