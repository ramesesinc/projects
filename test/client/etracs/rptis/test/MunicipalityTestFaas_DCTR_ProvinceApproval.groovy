class MunicipalityTestFaas_DCTR_ProvinceApproval
{
    public static void runTest(faas){
        println '='*50
        println '[municipality] Data capture FAAS, Simple Transfer Test and Province Approval'
        println '='*50

        def provhelper = ProvinceTestProxy.create('RPTISTestHelperService')
        def munihelper = MunicipalityTestProxy.create('RPTISTestHelperService')

        if (!faas.objid){
            faas.objid = 'DC' + faas.iparcel
        }

        /*=================================================
        * MUNICIPALITY: CREATE DATACAPTURE FAAS 
        =================================================*/
        println '[municipality] Capturing FAAS'
        def munisvc = MunicipalityTestProxy.create('RPTISTestFAASDataCaptureService')
        munisvc.createDataCapture(faas)
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
        munisvc = MunicipalityTestProxy.create('RPTISMunicipalityTestTransferFAASService')
        def task = munisvc.createSimpleTransferFaas(faas)
        def trfaas = [objid:task.objid]

        task = munisvc.doReceive(task)
        task = munisvc.doTaxmapping(task)
        task = munisvc.doAppraisal(task)
        task = munisvc.doRecommenderSubmitToProvince(task)
        println '[municipality] Simple Transfer approved.'


        /*=================================================
        * MUNICIPALITY: TEST MUNICIPALITY SUBMISSION TO PROVINCE SIMPLE TRANSFER FAAS 
        =================================================*/
        munisvc.testSubmittedToProvinceOnlineFaas(trfaas)
        println 'Test Completed.'
    }
}
