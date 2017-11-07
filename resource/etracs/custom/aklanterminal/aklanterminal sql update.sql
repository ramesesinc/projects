alter table ticket add refno varchar(25);

update etracs254_caticlan_terminal.ticket t, etracs254_caticlan.cashreceipt c set 
	t.refno = c.receiptno 
where t.refid = c.objid;

create index ix_refno on ticket(refno);


	
