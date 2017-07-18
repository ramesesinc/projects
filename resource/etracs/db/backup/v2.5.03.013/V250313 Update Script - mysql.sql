alter table faas add column preveffectivity varchar(10);


alter table structurematerial add column display int;
alter table structurematerial add column idx int;

update structurematerial set display = 1, idx = 0;

	

ALTER TABLE `rptledger`
DROP INDEX `ix_rptledger_tdno` ,
ADD UNIQUE INDEX `ux_rptledger_tdno` USING BTREE (`tdno`);