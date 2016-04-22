class MunicipalityTestConsolidation_ProvinceSubmission
{
    public static void runTest(faas1){
        println '='*50
        println '[municipality] Consolidation Create and Delete Test'

        def provhelper = ProvinceTestProxy.create('RPTISTestHelperService')
        def munihelper = MunicipalityTestProxy.create('RPTISTestHelperService')

        def faas2 = [isection:faas1.isection, iparcel:faas1.iparcel+1]
        def cfaas = [isection:faas2.isection, iparcel:faas2.iparcel+1]

        /*=================================================
        * MUNICIPALITY: CREATE DATACAPTURE FAAS 
        =================================================*/
        println '='*50
        println '[municipality] CAPTURE FAAS 1 for Consolidation -> ' + faas1 
        def munisvc = MunicipalityTestProxy.create('RPTISTestFAASDataCaptureService')
        faas1 = munisvc.createDataCapture(faas1)
        munisvc.submitDataCaptureForApproval(faas1)
        println munisvc.testSubmittedDataCapture(faas1)
        munisvc.approveDataCapture(faas1)
        println munisvc.testApprovedDataCapture(faas1)
        println '[municipality] Consolidated FAAS 1 created'

        println '[municipality] CAPTURE FAAS 2 for Consolidation -> ' + faas2
        faas2 = munisvc.createDataCapture(faas2)
        munisvc.submitDataCaptureForApproval(faas2)
        println munisvc.testSubmittedDataCapture(faas2)
        munisvc.approveDataCapture(faas2)
        println munisvc.testApprovedDataCapture(faas2)
        println '[municipality] Consolidated FAAS 2 created'


        /*=================================================
        * MUNICIPALITY: APPROVE DATA CAPTURE LEDGER 
        =================================================*/
        println '='*50
        println '[municipality] APPROVE FAAS Ledger'
        munisvc = MunicipalityTestProxy.create('RPTISTestLedgerService')
        println munisvc.testPendingLedgerFromFaas(faas1)
        def ledger1 = munisvc.approveLedgerFromFaas(faas1, true)
        println munisvc.testApprovedLedger(ledger1)
        println '[municipality] FAAS Ledger 1 approved and tested'

        println munisvc.testPendingLedgerFromFaas(faas2)
        def ledger2 = munisvc.approveLedgerFromFaas(faas2, true)
        println munisvc.testApprovedLedger(ledger2)
        println '[municipality] FAAS Ledger 2 approved and tested'


        /*=================================================
        * MUNICIPALITY: CREATE CONSOLIDATION 
        =================================================*/
        println '='*50
        println '[municipality] CREATE Consolidation'
        munisvc = MunicipalityTestProxy.create('RPTISMunicipalityTestConsolidationService')
        def consolidation = munisvc.createConsolidation(cfaas)
        def task = munisvc.doReceive(consolidation)
        task = munisvc.doExamination(task)
        task = munisvc.doTaxmapping(task, faas1, faas2)
        task = munisvc.doAppraisal(task)
        task = munisvc.doRecommender(task)
        munisvc.submitToProvince(task)
        println '[municipality] Consolidation creation and submission to province tested'
    }
}
