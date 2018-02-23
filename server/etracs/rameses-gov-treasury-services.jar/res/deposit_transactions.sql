[reverseRemittance]
UPDATE cashreceipt SET remittanceid = NULL;
UPDATE cashreceiptpayment_noncash SET remittancefundid = NULL;
DELETE FROM remittance_af;
DELETE FROM remittance_fund;
DELETE FROM remittance;


[reverseLiquidation]
UPDATE cashreceiptpayment_noncash SET liquidationfundid = NULL;
UPDATE remittance_fund SET liquidationfundid = NULL;
UPDATE remittance SET liquidationid = NULL;
DELETE FROM liquidation_fund;
DELETE FROM liquidation;

UPDATE paymentcheck SET depositid=NULL, depositfundid=NULL, bankdepositid = NULL;
UPDATE liquidation_fund SET depositfundid = NULL;
UPDATE liquidation SET depositid = NULL;
DELETE FROM bankdeposit;
DELETE FROM fundtransfer;
DELETE FROM deposit_fund_transfer;
DELETE FROM deposit_fund;
DELETE FROM deposit;

[reverseDeposit]
UPDATE paymentcheck SET depositid=NULL, depositfundid=NULL, bankdepositid = NULL;
UPDATE liquidation_fund SET depositfundid = NULL;
UPDATE liquidation SET depositid = NULL;
DELETE FROM bankdeposit;
DELETE FROM fundtransfer;
DELETE FROM deposit_fund_transfer;
DELETE FROM deposit_fund;
DELETE FROM deposit;