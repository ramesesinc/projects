
/* 2015-01-08 */
INSERT INTO sys_usergroup (
	objid, title, domain, userclass, orgclass, role
) VALUES (
	'ENTITY.ADMIN', 'ENTITY ADMIN', 'ENTITY', 'usergroup', NULL, 'ADMIN'
);


/* 2015-01-09 */
create index ix_dtposted on bankdeposit (dtposted);
create index ix_cashier_objid on bankdeposit (cashier_objid);
create index ix_cashier_name on bankdeposit (cashier_name);

create index ix_dtposted on liquidation (dtposted);
create index ix_liquidatingofficer_objid on liquidation (liquidatingofficer_objid);
create index ix_liquidatingofficer_name on liquidation (liquidatingofficer_name);

update itemaccount set type='REVENUE' where type='INCOME';
update itemaccount set type='PAYABLE' where type='LIABILITY';


/* 2015-01-13 */
ALTER TABLE income_summary ADD collectorid VARCHAR(50) NULL;

CREATE INDEX ix_collectorid ON income_summary (collectorid);

ALTER TABLE income_summary DROP PRIMARY KEY;
ALTER TABLE income_summary MODIFY COLUMN `refdate` DATE NOT NULL;
ALTER TABLE income_summary MODIFY COLUMN `fundid`  VARCHAR(50) NOT NULL;
ALTER TABLE income_summary ADD PRIMARY KEY (`refid`, `refdate`, `fundid`, `acctid`, `orgid`);


insert into creditmemo (
	objid, state, refdate, refno, amount, particulars, 
	type_objid, type_title, payername, controlno, dtissued 
) 
select 
	c.objid, 
	case 
		when c.state='APPROVED' then 'POSTED' 
		else c.state 
	end as state,
	c.refdate, c.refno, c.amount, c.remarks as particulars,
	'STANDARD' as type_objid, 'STANDARD (General)' as type_title, 
	c.payee as payername, c.refno as controlno, c.refdate as dtissued  
from directcash_collection c;


insert into creditmemoitem (
	objid, parentid, item_objid, amount 
) 
select 
	objid, parentid, item_objid, amount  
from directcash_collection_item
;


insert into income_summary (
	refid, refdate, refno, reftype, acctid, fundid, amount, orgid  
) 
select 
	cm.objid, cm.refdate, cm.refno, cm.type_objid as reftype, 
	cmi.item_objid as acctid, ia.fund_objid as fundid, sum(cmi.amount) as amount,
	'169' as orgid  
from directcash_collection dc 
	inner join creditmemo cm on dc.objid=cm.objid  
	inner join creditmemoitem cmi on cm.objid=cmi.parentid 
	inner join itemaccount ia on cmi.item_objid=ia.objid 
group by cm.objid, cm.refdate, cm.refno, cm.type_objid, cmi.item_objid, ia.fund_objid 
;


DELETE FROM income_summary WHERE reftype='liquidation'; 

INSERT INTO income_summary ( 
   refid, refdate, acctid, fundid, 
   amount, refno, reftype, orgid, 
   collectorid 
) 
SELECT * FROM ( 
	SELECT 
	   r.objid AS refid, 
	   DATE_FORMAT(r.dtposted, '%Y-%m-%d') AS refdate,
	   ci.item_objid AS acctid, 
	   ri.fund_objid AS fundid,
	   SUM( ci.amount) AS amount,
	   r.txnno AS refno,
	   'remittance' AS reftype,
	   c.org_objid AS orgid,
	   r.collector_objid AS collectorid 
	FROM liquidation lq
	   INNER JOIN liquidation_remittance lr ON lq.objid=lr.liquidationid
	   INNER JOIN remittance r ON r.objid=lr.objid
	   INNER JOIN remittance_cashreceipt rc ON rc.remittanceid=r.objid
	   INNER JOIN cashreceipt c ON c.objid=rc.objid
	   INNER JOIN cashreceiptitem ci ON c.objid=ci.receiptid
	   INNER JOIN itemaccount ri ON ci.item_objid=ri.objid
	   LEFT JOIN cashreceipt_void cv ON cv.receiptid=c.objid    
	WHERE r.dtposted BETWEEN '2013-01-01 00:00:00' AND 
							 '2015-02-01 00:00:00'
		AND cv.objid IS NULL 
	GROUP BY  
	   r.objid, 
	   DATE_FORMAT(r.dtposted, '%Y-%m-%d'),  
	   ci.item_objid, 
	   ri.fund_objid,
	   r.txnno,
	   c.org_objid, 
	   r.collector_objid 
)bt 
WHERE NOT EXISTS (
	SELECT 1 FROM income_summary 
	WHERE refid=bt.refid AND acctid=bt.acctid AND 
		fundid=bt.fundid AND orgid=bt.orgid 
)
;

