RENAME TABLE remittance_checkpayment TO remittance_noncashpayment;
RENAME TABLE liquidation_checkpayment TO liquidation_noncashpayment;
RENAME TABLE remittance_afserial TO remittance_af;

insert into remittance_af select * from remittance_cashticket;
drop table remittance_cashticket;