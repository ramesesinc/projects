def faas = [isection:1, iparcel:20]
ProvinceTestFaas_DC_Delete.runTest(faas)
ProvinceTestFaas_DCTR_Approve.runTest(faas)

println 'Test completed.'