[getList]
SELECT pl.*, a.title AS type, (pl.amtdue-pl.amtpaid-pl.amtcredit) AS balance  
FROM patientchart_ledger pl
LEFT JOIN activity a ON pl.item_type=a.objid
WHERE parentid=$P{objid}

[findUnpaidBalance]
SELECT SUM(pl.amtdue - (pl.amtpaid+pl.amtcredit)) AS balance
FROM patientchart_ledger pl
INNER JOIN patientchart p ON pl.parentid=p.objid 
WHERE p.patient_objid = $P{objid}
GROUP BY p.patient_objid 

