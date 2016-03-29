class MunicipalityTestSubdivision_CreateDelete
{
    public static void runTest(faas){
        println '='*50
        println '[municipality] Subdivision Create and Delete Test'
        println '='*50

        def provhelper = ProvinceTestProxy.create('RPTISTestHelperService')
        def munihelper = MunicipalityTestProxy.create('RPTISTestHelperService')

        /*=================================================
        * MUNICIPALITY: CREATE DATACAPTURE FAAS 
        =================================================*/
        println '[municipality] Capture FAAS for Subdivision'
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
        println '[municipality] FAAS Ledger approved and tested'


        /*=================================================
        * MUNICIPALITY: CREATE SUBDIVISION 
        =================================================*/
        println '[municipality] Creating Subdivision'
        munisvc = MunicipalityTestProxy.create('RPTISMunicipalityTestSubdivisionService')
        def subdivision = munisvc.createSubdivision()
        def task = munisvc.doReceive(subdivision)
        println 'doReceive...done'
        task = munisvc.doExamination(task)
        println 'doExamination...done'
        task = munisvc.doTaxmapping(task, faas)
        println 'doTaxmapping...done'
        task = munisvc.doAppraisal(task)
        println 'doAppraisal...done'
        task = munisvc.doReturnExaminer(task)
        println 'doReturnExaminer...done'
        task = munisvc.doReturnReceiver(task)
        println 'doReturnReceiver...done'
        munisvc.deleteSubdivision(task)
        println '[municipality] Subdivision creation and deletion tested.'
    }
}
