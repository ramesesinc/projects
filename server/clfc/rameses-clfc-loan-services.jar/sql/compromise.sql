[getList]
SELECT * FROM compromise
ORDER BY seqno

[getListByLedgerid]
SELECT * FROM compromise
WHERE ledgerid = $P{ledgerid}
ORDER BY dtcreated

[getOffers]
SELECT * FROM compromise_offer
WHERE parentid = $P{objid}
ORDER BY term_months, term_days

[countOffer]
SELECT COUNT(objid) FROM compromise_offer
WHERE parentid = $P{objid}

[changeState]
UPDATE compromise SET state = $P{state}
WHERE objid = $P{objid}