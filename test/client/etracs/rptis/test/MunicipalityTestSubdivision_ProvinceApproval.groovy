class MunicipalityTestSubdivision_ProvinceApproval
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
        task = munisvc.doAppraisal(task)
        task = munisvc.doRecommender(task)
        munisvc.submitToProvince(task)
        println '[municipality] Subdivision creation and submission to province tested'


        /*=================================================
        * PROVINCE: APPROVED SUBMITTED SUBDIVISION
        =================================================*/
        println '='*50
        println '[province] APPROVING submitted subdivision from municipality'
        def provsvc = ProvinceTestProxy.create('RPTISProvinceTestMunicipalitySDForApproval')
        TestHelper.waitForSubdivision(subdivision, provhelper)
        def munisubdivision = munisvc.getSubdivisionForTesting(subdivision)
        println provsvc.testSubmittedSubdivisionForApproval(subdivision, munisubdivision)

        subdivision += provsvc.openSubdivision(subdivision)
        task = provsvc.doReceive(subdivision)
        println 'doReceive...done'
        task = provsvc.doExamination(task)
        println 'doExamination...done'
        task = provsvc.doTaxmapping(task)
        println 'doTaxmapping...done'
        task = provsvc.doAppraisal(task)
        println 'doAppraisal...done'
        task = provsvc.doApprover(task)
        println 'doApprover...done'
        provsvc.approveSubdivision(task)
        println 'approveSubdivision...done'
        println provsvc.testApprovedSubdivision(subdivision)


        /*=================================================
        * MUNICIPALITY: TEST APPROVED SUBDIVISION
        =================================================*/
        println '='*50
        println '[municipality] Test APPROVED subdivision from province'
        TestHelper.waitForApprovedFaas(subdivision, munihelper)
        println munisvc.testApprovedSubdivision(subdivision)
        println 'Test Completed.'
    }
}
