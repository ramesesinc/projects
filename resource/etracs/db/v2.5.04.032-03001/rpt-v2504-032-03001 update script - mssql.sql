/* v2.5.04.032-03001 */
alter table cancelledfaas add `online` int;
go 

alter table cancelledfaas add `lguid` varchar(50)
go

alter table cancelledfaas add `lasttaxyear` int
go 

alter table cancelledfaas alter column `remarks` text null;
go 