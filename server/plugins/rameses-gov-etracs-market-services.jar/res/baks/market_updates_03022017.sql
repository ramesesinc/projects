ALTER TABLE market_account DROP COLUMN term;
ALTER TABLE market_account ADD COLUMN payfrequency VARCHAR(50);
UPDATE market_account SET payfrequency='DAILY';
ALTER TABLE market_rentalunit DROP COLUMN term;
ALTER TABLE market_rentalunit ADD COLUMN payfrequency VARCHAR(50);
UPDATE market_rentalunit SET payfrequency='DAILY';
ALTER TABLE market_account ADD COLUMN remarks VARCHAR(255);
ALTER TABLE market_account ADD COLUMN dateclosed DATE;
ALTER TABLE market_account DROP COLUMN padlockdate;

ALTER TABLE market_rentalunit DROP COLUMN locationid;