

alter table waterworks_account_consumption add amount decimal(16,2);
alter table waterworks_account_consumption add amtpaid decimal(16,2);
UPDATE waterworks_account_consumption SET amtpaid=0,amount=rate;

ALTER TABLE waterworks_account_consumption DROP ledgerid; 

ALTER TABLE waterworks_payment_item ADD txntype VARCHAR(50);

