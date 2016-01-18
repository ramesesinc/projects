[getRyListLAND]
SELECT s.* 
FROM landrysetting s
	INNER JOIN rysetting_lgu r ON s.objid = r.rysettingid
WHERE r.lguid = $P{lguid}
  AND s.ry > $P{ry}


[getRyListBLDG]
SELECT s.* 
FROM bldgrysetting s
	INNER JOIN rysetting_lgu r ON s.objid = r.rysettingid
WHERE r.lguid = $P{lguid}
  AND s.ry > $P{ry}  


[getRyListMACH]
SELECT s.* 
FROM machrysetting s
	INNER JOIN rysetting_lgu r ON s.objid = r.rysettingid
WHERE r.lguid = $P{lguid}
  AND s.ry > $P{ry}    


[getRyListPLANTTREE]
SELECT s.* 
FROM planttreerysetting s
	INNER JOIN rysetting_lgu r ON s.objid = r.rysettingid
WHERE r.lguid = $P{lguid}
  AND s.ry > $P{ry}    


[getRyListMISC]
SELECT s.* 
FROM miscrysetting s
	INNER JOIN rysetting_lgu r ON s.objid = r.rysettingid
WHERE r.lguid = $P{lguid}
  AND s.ry > $P{ry}    



[findCurrentRevisedLandRpu]
SELECT rpu.objid, rpu.realpropertyid
FROM rpu rpu
	INNER JOIN landrpu lr ON rpu.objid = lr.objid 
WHERE rpu.objid = $P{objid}
 AND rpu.ry = $P{ry}  

[findCurrentRevisedLandRpuByPin]
SELECT rpu.objid, rpu.realpropertyid
FROM rpu rpu
	INNER JOIN landrpu lr ON rpu.objid = lr.objid 
	inner join realproperty rp on rpu.realpropertyid = rp.objid
WHERE rp.pin = $P{pin}
 AND rpu.ry = $P{ry}  


[findRevisedLandRpu]
SELECT rpu.objid, rpu.realpropertyid
FROM rpu rpu
	INNER JOIN landrpu lr ON rpu.objid = lr.objid 
WHERE rpu.previd = $P{previd}
 AND rpu.ry = $P{ry}   


[findRevisedLandByRealPropertyId]
SELECT rpu.objid, rpu.realpropertyid
FROM rpu rpu
	INNER JOIN landrpu lr ON rpu.objid = lr.objid 
WHERE rpu.realpropertyid = $P{realpropertyid}
 AND rpu.ry = $P{ry}