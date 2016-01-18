########################################################
# BusinessInfoService
########################################################
[getList]
SELECT DISTINCT b.*  
FROM 
(
	SELECT xb.objid,xb.state,xb.owner_objid,xb.owner_name,xb.businessname,xb.address_text,xb.activeyear,xb.bin,
    bp.permitno, bp.expirydate, bp.state AS permitstate, bp.version 
	FROM business xb
    LEFT JOIN business_permit bp ON bp.applicationid=xb.currentapplicationid
	WHERE xb.owner_name LIKE $P{searchtext} 
UNION 
    SELECT xb.objid,xb.state,xb.owner_objid,xb.owner_name,xb.businessname,xb.address_text,xb.activeyear,xb.bin,
    bp.permitno, bp.expirydate, bp.state AS permitstate, bp.version
    FROM business xb
    LEFT JOIN business_permit bp ON bp.applicationid=xb.currentapplicationid 
	WHERE xb.businessname LIKE $P{searchtext} 
UNION
    SELECT xb.objid,xb.state,xb.owner_objid,xb.owner_name,xb.businessname,xb.address_text,xb.activeyear,xb.bin,
    bp.permitno, bp.expirydate, bp.state AS permitstate, bp.version
    FROM business xb
    LEFT JOIN business_permit bp ON bp.applicationid=xb.currentapplicationid 
	WHERE xb.bin LIKE $P{searchtext} 
) b
WHERE NOT(b.objid IS NULL)
${filter}


[getListForVerification]
SELECT objid,state,owner_name,businessname,address_text,activeyear
FROM business 
WHERE businessname LIKE $P{businessname}
ORDER BY businessname

[getSearchList]
SELECT a.* FROM
(SELECT DISTINCT 
    xb.objid, xb.state, xb.owner_name AS ownername, 
    xb.owner_address_text AS owneraddress, xb.businessname, 
    xb.address_text AS address, xb.activeyear, xb.bin 
FROM ( 
    SELECT objid, MAX(activeyear) AS activeyear  
    FROM business b  
    WHERE b.owner_name LIKE $P{ownername} AND b.state NOT IN ('CANCELLED','RETIRED') 
    GROUP BY objid 
    
    UNION
    
    SELECT objid, MAX(activeyear) AS activeyear 
    FROM business b 
    WHERE b.businessname LIKE $P{tradename} AND b.state NOT IN ('CANCELLED','RETIRED') 
    GROUP BY objid 
    
    UNION 
    
    SELECT objid, MAX(activeyear) AS activeyear  
    FROM business b 
    WHERE b.bin LIKE $P{bin} AND b.state NOT IN ('CANCELLED','RETIRED') 
    GROUP BY objid 
)bt 
INNER JOIN business xb ON (bt.objid=xb.objid AND bt.activeyear=xb.activeyear)) a  


[updatePermit]
UPDATE business SET 
    state='ACTIVE', apptype=$P{apptype}, permit_objid=$P{permitid} 
WHERE 
    objid=$P{businessid}

[findByBIN]
SELECT objid, businessname, owner_name FROM business WHERE bin = $P{bin}

[findBusinessInfoByBIN]
SELECT 
    objid AS businessid, businessname, tradename, address_text,
    owner_objid,  owner_name, owner_address_text, bin 
FROM business 
WHERE bin = $P{bin}

[updateApplicationId]
UPDATE business SET currentapplicationid=$P{applicationid} WHERE objid=$P{businessid}

[getListByOwner]
SELECT * FROM business WHERE owner_objid=$P{ownerid}

[updateOnRelease]
UPDATE business SET state=$P{state}, activeyear=$P{activeyear} WHERE objid=$P{objid}  

[updateForRetire]
UPDATE business SET state=$P{state}, apptype=$P{apptype} WHERE objid=$P{objid}  
