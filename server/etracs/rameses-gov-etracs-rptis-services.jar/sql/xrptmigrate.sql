[deleteQuarterlyLedgerItems]
DELETE FROM rptledgeritem_qtrly

[deleteLedgerItems]
DELETE FROM rptledgeritem


[getUnpostedMigratedLedgers]
SELECT objid FROM rptledger rl
WHERE rl.state IN ('APPROVED') 
  AND NOT EXISTS(SELECT * FROM rptledgeritem WHERE rptledgerid = rl.objid)