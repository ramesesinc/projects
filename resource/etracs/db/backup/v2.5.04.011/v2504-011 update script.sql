/* FAAS : add previous administrator info */
alter table faas add column prevadministrator varchar(200);


update rptledgerfaas rlf, rptledger rl set 
		rlf.state = 'APPROVED' 
where rl.state = 'APPROVED' 
 and rlf.state = 'PENDING';