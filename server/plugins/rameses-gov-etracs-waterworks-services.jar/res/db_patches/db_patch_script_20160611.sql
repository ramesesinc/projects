

alter table waterworks_account_consumption add amount decimal(16,2);
alter table waterworks_account_consumption add amtpaid decimal(16,2);

UPDATE waterworks_account_consumption c, waterworks_account_ledger l 
SET c.amount=l.amount, c.amtpaid=l.amtpaid WHERE c.ledgerid=l.objid;

--ALTER TABLE waterworks_account_consumption DROP ledgerid; 

ALTER TABLE waterworks_payment_item ADD txntype VARCHAR(50);

