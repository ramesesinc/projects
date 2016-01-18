[getList]
SELECT * FROM patientchart_allergy WHERE parentid=$P{objid}

[getLookupDrugAllergen]
SELECT objid, code,name FROM drug WHERE name LIKE $P{searchtext}

[getLookupFoodAllergen]
SELECT objid,code,name FROM foodtype WHERE name LIKE $P{searchtext}

[getLookupEnvironmentAllergen]
SELECT objid,code,name FROM environmenttype WHERE name LIKE $P{searchtext}

[getDrugAllergies]
SELECT * FROM patientchart_allergy WHERE parentid=$P{objid} AND allergenclass='DRUG'
