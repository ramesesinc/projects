[getList]
SELECT 
	r.objid, r.state, r.pintype, r.txnno, r.section, b.name AS barangay 
FROM resection r 
	INNER JOIN barangay b ON r.barangayid = b.objid 
where 1=1 ${filters}	
ORDER BY r.txnno DESC 


[findBarangayLastSection]
SELECT MAX(section) AS section
FROM realproperty 
WHERE barangayid = $P{barangayid}
  AND state = 'CURRENT' 


[getAffectedRpus]
SELECT
	r.rputype,
	f.objid AS prevfaasid,
	r.objid AS prevrpuid,
	rp.objid AS prevrpid,
	f.tdno,
	r.fullpin
FROM faas f 
	INNER JOIN rpu r ON f.rpuid = r.objid
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
WHERE rp.barangayid = $P{barangayid}
  AND rp.section = $P{section}
  AND f.state = 'CURRENT' 
  AND r.state = 'CURRENT'
  AND rp.state = 'CURRENT'
ORDER BY r.fullpin   


[getResectionItems]
SELECT *
FROM resectionitem 
WHERE resectionid = $P{resectionid}
ORDER BY newsection


[getResectionAffectedRpus]
SELECT
	arpu.*,
	r.rputype,
	f.objid AS prevfaasid,
	r.objid AS prevrpuid,
	rp.objid AS prevrpid,
	f.tdno,
	r.fullpin
FROM resectionaffectedrpu arpu
	INNER JOIN faas f ON arpu.prevfaasid = f.objid
	INNER JOIN rpu r ON f.rpuid = r.objid
	INNER JOIN realproperty rp ON r.realpropertyid = rp.objid 
WHERE arpu.resectionid = $P{resectionid}
ORDER BY r.fullpin 



[findAffectedLandByPrevId]
SELECT *
FROM resectionaffectedrpu arpu
WHERE arpu.prevrpid = $P{prevrpid}
 AND arpu.rputype = 'land' 
  



[deleteResectionItems]
DELETE FROM resectionitem WHERE resectionid = $P{objid}

[deleteResectionAffectedRpus]
DELETE FROM resectionaffectedrpu WHERE resectionid = $P{objid}



[findSection]
SELECT MAX(section) AS section
FROM realproperty 
WHERE barangayid = $P{barangayid}
  AND section = $P{section}


[findState]  
SELECT state FROM resection WHERE objid = $P{objid}


  
[clearAffectedRpuNewRefIds]
UPDATE resectionaffectedrpu SET 
	newfaasid = null, newrpuid = null, newrpid = null 
WHERE resectionid = $P{objid}

[approveResection]
UPDATE resection SET state = 'APPROVED' WHERE objid = $P{objid}


[updateState]
UPDATE resection SET state = $P{state} WHERE objid = $P{objid} AND state = $P{prevstate}


[updateAffectedRpu]
UPDATE resectionaffectedrpu SET 
	newfaasid = $P{newfaasid},
	newrpuid = $P{newrpuid},
	newrpid = $P{newrpid},
	newtdno = $P{newtdno},
	newutdno = $P{newutdno}
WHERE objid = $P{objid}	


[updateFaasTdInfo]
UPDATE faas SET 
	tdno = $P{newtdno},
	utdno = $P{newutdno}
WHERE objid = $P{newfaasid}

#===============================================================
#
#  ASYNCHRONOUS APPROVAL SUPPORT 
#
#================================================================

[findFaasByNewRpuId]
SELECT 
	r.ry AS rpu_ry, 
	rp.barangayid AS rp_barangay_objid
FROM rpu r 
	INNER JOIN realproperty rp ON r.realpropertyid = rp.objid 
WHERE r.objid =  $P{newrpuid}	



[findTrackingNo]
SELECT trackingno FROM rpttracking WHERE objid = $P{objid}


