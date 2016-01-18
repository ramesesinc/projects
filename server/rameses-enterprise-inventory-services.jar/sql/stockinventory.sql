[findAvailable]
SELECT * FROM 
stockinventory 
WHERE item_objid = $P{objid}
AND unit = $P{unit}
AND qtybalance >= $P{qty}
