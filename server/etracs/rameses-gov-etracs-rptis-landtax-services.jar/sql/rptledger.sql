[getList]
SELECT 
    ${columns}
FROM rptledger rl 
    INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
    INNER JOIN barangay b ON rl.barangayid = b.objid 
WHERE 1=1
${fixfilters}
${filters}
${orderby}
