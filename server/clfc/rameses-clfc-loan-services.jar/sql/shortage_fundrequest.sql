[getList]
SELECT * FROM shortage_fundrequest
ORDER BY dtrequested DESC

[findForapprovalFundRequestByRemittanceid]
SELECT * FROM shortage_fundrequest
WHERE remittanceid = $P{remittanceid}
	AND state = 'FOR_APPROVAL'

[changeState]
UPDATE shortage_fundrequest SET state = $P{state}
WHERE objid = $P{objid}