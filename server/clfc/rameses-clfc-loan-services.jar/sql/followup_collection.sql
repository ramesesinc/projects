[getList]
SELECT sc.*, llb.billdate
FROM special_collection sc
INNER JOIN followup_collection fc ON sc.objid=fc.objid
INNER JOIN loan_ledger_billing llb ON sc.billingid=llb.objid
WHERE sc.state = $P{state}
ORDER BY sc.dtrequested

[getFollowupCollectionByBillingid]
SELECT sc.objid FROM special_collection sc
INNER JOIN followup_collection fc ON sc.objid=fc.objid
WHERE sc.billingid=$P{objid}
ORDER BY sc.dtrequested