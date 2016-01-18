[getLedger]
SELECT ma.objid, ma.owner_objid, ma.owner_name, 
ma.owner_address, mru.name, m.name AS market_name  
FROM marketaccount ma
INNER JOIN market_rentalunit mru ON ma.rentalunit_objid=mru.objid
INNER JOIN market m ON mru.marketid=m.objid 
WHERE ma.owner_objid=$P{ownerid}

[findAccount]
SELECT ma.objid, mru.name, m.name AS market_name, ma.rate, 
mst.objid AS section_objid, mst.objid AS section_name,
mru.unittype, ma.startdate   
FROM marketaccount ma
INNER JOIN market_rentalunit mru ON ma.rentalunit_objid=mru.objid
INNER JOIN marketsection mst ON mru.section_objid=mst.objid 
INNER JOIN market m ON mru.marketid=m.objid 
WHERE ma.objid=$P{acctid}

[getLedgerList]
SELECT a.* FROM 
(SELECT ma.objid, ma.owner_objid, ma.owner_name, ma.acctno, ma.rate, 
ma.owner_address, mru.name AS rentalunit, m.name AS market_name  
FROM marketaccount ma
INNER JOIN market_rentalunit mru ON ma.rentalunit_objid=mru.objid
INNER JOIN market m ON mru.marketid=m.objid 
WHERE ma.owner_name LIKE $P{ownername}
UNION
SELECT ma.objid, ma.owner_objid, ma.owner_name, ma.acctno, ma.rate,
ma.owner_address, mru.name  AS rentalunit, m.name AS market_name  
FROM marketaccount ma
INNER JOIN market_rentalunit mru ON ma.rentalunit_objid=mru.objid
INNER JOIN market m ON mru.marketid=m.objid 
WHERE ma.acctno LIKE $P{acctno}) a
