[reverseDeposit]
UPDATE paymentcheck SET depositid=NULL, depositfundid=NULL, bankdepositid = NULL;
UPDATE liquidation_fund SET depositfundid = NULL;
UPDATE liquidation SET depositid = NULL;
DELETE FROM bankdeposit;
DELETE FROM fundtransfer;
DELETE FROM deposit_fund_transfer;
DELETE FROM deposit_fund;
DELETE FROM deposit;