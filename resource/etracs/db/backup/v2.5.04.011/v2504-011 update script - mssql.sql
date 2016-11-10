/* FAAS : add previous administrator info */
alter table faas add prevadministrator varchar(200);


update rlf set 
		rlf.state = 'APPROVED' 
from rptledgerfaas rlf, rptledger rl 
where rl.state = 'APPROVED' 
 and rlf.state = 'PENDING';


 