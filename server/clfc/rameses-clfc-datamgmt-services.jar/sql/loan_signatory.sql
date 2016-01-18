[getList]
SELECT * FROM loan_signatory

[approve]
UPDATE loan_signatory SET state = 'APPROVED'
WHERE objid = $P{objid}