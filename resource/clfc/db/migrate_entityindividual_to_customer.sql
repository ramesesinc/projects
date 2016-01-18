INSERT IGNORE INTO customer (
	objid, state, `mode`, dtcreated, createdby, custno, branchid,
	`name`, lastname, firstname, middlename, gender, birthdate, citizenship,
	civilstatus, address, previousaddress, dtmodified, modifiedby
) 
SELECT 
	i.objid, 'ACTIVE' AS state, 'ONLINE' AS `mode`, bt.dtfiled AS dtcreated, l.createdby, CONCAT('CLFC00',e.entityno) AS custno, 'CLFC' AS branchid, 
	e.entityname AS `name`, i.lastname, i.firstname, i.middlename, i.gender, i.birthdate, i.citizenship, 
	i.civilstatus, e.address, NULL AS previousaddress, bt.dtfiled AS dtmodified, l.createdby AS modifiedby   
FROM (SELECT * FROM loanapp_capture WHERE dtfiled BETWEEN '2014-05-02 00:00:00' AND '2014-05-02 23:59:59')bt 
	INNER JOIN loanapp l ON bt.objid=l.objid 
	INNER JOIN loanapp_borrower lb ON bt.objid=lb.parentid 
	INNER JOIN entityindividual i ON lb.borrowerid=i.objid 
	INNER JOIN entity e ON i.objid=e.objid
