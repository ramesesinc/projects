class ProvinceTestFaas_DC_Delete
{
    public static void runTest(faas){
        println '='*50
        println '[province] Data capture FAAS and Delete Test'
        println '='*50

        def svc = ProvinceTestProxy.create('RPTISProvinceTestDataCaptureFAASService')
        faas.objid = 'DC' + faas.iparcel
        svc.createDataCapture(faas)

        svc.submitDataCaptureForApproval(faas)
        println svc.testSubmittedDataCapture(faas)

        faas += svc.deleteDataCapturedFaas(faas)
        println svc.testDeletedDataCapturedFaas(faas)
        println '='*50 
    }

}
