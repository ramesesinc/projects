class MunicipalityTestSubdivision_ProvinceSubmission
{
    public static void runTest(faas){
        println '='*50
        println '[municipality] Subdivision Create and Delete Test'

        def provhelper = ProvinceTestProxy.create('RPTISTestHelperService')
        def munihelper = MunicipalityTestProxy.create('RPTISTestHelperService')

        /*=================================================
        * MUNICIPALITY: CREATE DATACAPTURE FAAS 
        =================================================*/
        println '='*50
        println '[municipality] CAPTURE FAAS for Subdivision'
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
        println '='*50
        println '[municipality] APPROVE FAAS Ledger'
        munisvc = MunicipalityTestProxy.create('RPTISTestLedgerService')
        println munisvc.testPendingLedgerFromFaas(faas)
        def ledger = munisvc.approveLedgerFromFaas(faas, true)
        println munisvc.testApprovedLedger(ledger)
        println '[municipality] FAAS Ledger approved and tested'


        /*=================================================
        * MUNICIPALITY: CREATE SUBDIVISION 
        =================================================*/
        println '='*50
        println '[municipality] CREATE Subdivision'
        munisvc = MunicipalityTestProxy.create('RPTISMunicipalityTestSubdivisionService')
        def subdivision = munisvc.createSubdivision()
        def task = munisvc.doReceive(subdivision)
        task = munisvc.doExamination(task)
        task = munisvc.doTaxmapping(task, faas)
        munisvc.testTaxmapping(subdivision)
        task = munisvc.doAppraisal(task)
        task = munisvc.doRecommender(task)
        munisvc.submitToProvince(task)
        println '[municipality] Subdivision creation and submission to province tested'

    }
}
