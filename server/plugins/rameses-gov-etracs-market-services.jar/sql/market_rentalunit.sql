[getList]
SELECT b.* FROM
(SELECT mr.*,
ms.title AS section_title,
mb.name AS bldg_name,
ma.owner_name  AS lessee_name,
ma.rate AS lessee_rate
FROM market_rentalunit mr
INNER JOIN marketsection ms ON mr.section_objid=ms.objid
LEFT JOIN market_building mb ON mr.bldg_objid=mb.objid
LEFT JOIN market_rentalunit_lessee mrl ON mrl.objid=mr.objid
LEFT JOIN marketaccount ma ON ma.objid=mrl.acctid
WHERE mr.marketid=$P{marketid}) b  ORDER BY b.code

[getAttributes]
SELECT mra.*,  ma.name AS attribute_name
FROM market_rentalunit_attribute mra
INNER JOIN marketattribute ma ON mra.attribute_objid=ma.objid
WHERE parentid=$P{objid}

[getLookup]
SELECT b.* FROM 
(SELECT mr.*, m.name AS market_name, 
ms.name AS section_name,
ms.title AS section_title,
mr.bldg_objid AS bldgid
FROM market_rentalunit mr
INNER JOIN market m ON mr.marketid=m.objid
INNER JOIN marketsection ms ON mr.section_objid=ms.objid
WHERE mr.name LIKE  $P{searchtext}) b

[getLookupAvailable]
SELECT b.* FROM 
(SELECT mr.*, m.name AS market_name, 
ms.name AS section_name,
ms.title AS section_title,
mr.bldg_objid AS bldgid
FROM market_rentalunit mr
INNER JOIN market m ON mr.marketid=m.objid
INNER JOIN marketsection ms ON mr.section_objid=ms.objid
WHERE  
	mr.objid NOT IN ( SELECT objid FROM market_rentalunit_lessee )
	AND mr.name LIKE  $P{searchtext} 
) b

[approve]
UPDATE market_rentalunit SET state='APPROVED' WHERE objid=$P{objid}


