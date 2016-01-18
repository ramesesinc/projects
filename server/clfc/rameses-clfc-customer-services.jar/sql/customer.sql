[getLookupCustomers]
SELECT c.* FROM customer c 
WHERE ${filter} ORDER BY c.name 

[findCustomer]
SELECT * FROM customer WHERE objid=$P{objid} 

[findCustomerByCustno]
SELECT * FROM customer WHERE custno = $P{custno}

[getConnections]
SELECT r.*
	-- r.objid, r.name, r.lastname, r.firstname, r.middlename, c.relationship
FROM customer_connection c 
	INNER JOIN customer r ON c.relaterid=r.objid 
WHERE c.principalid=$P{principalid} 
ORDER BY r.name 

[removePrincipalSpouse]
DELETE FROM customer_connection 
WHERE principalid=$P{principalid} AND relationship='SPOUSE'

[removeConnection]
DELETE FROM customer_connection 
WHERE principalid=$P{principalid} AND relaterid=$P{relaterid} 
