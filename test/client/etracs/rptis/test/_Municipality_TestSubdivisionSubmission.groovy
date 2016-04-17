def faas = [isection:2, iparcel:1]
MunicipalityTestFaas_DC_Delete.runTest(faas)

faas = [isection:2, iparcel:2]
MunicipalityTestFaas_DCTR_Approve.runTest(faas)

faas = [isection:2, iparcel:3]
MunicipalityTestFaas_DCTR_ProvinceApproval.runTest(faas)

faas = [isection:2, iparcel:4]
MunicipalityTestSubdivision_CreateDelete.runTest(faas)

faas = [isection:2, iparcel:5]
MunicipalityTestSubdivision_ProvinceSubmission.runTest(faas)
