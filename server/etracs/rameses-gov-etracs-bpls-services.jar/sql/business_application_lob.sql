[getList]
SELECT 
    bl.*, 
    lc.name AS classification_name, 
    lc.objid as classification_objid   
FROM business_application_lob bl
INNER JOIN business b ON b.objid=bl.businessid
INNER JOIN lob ON bl.lobid=lob.objid
INNER JOIN lobclassification lc ON lob.classification_objid=lc.objid 
WHERE bl.applicationid = $P{applicationid}

[removeList]
DELETE FROM business_application_lob WHERE applicationid=$P{applicationid}



