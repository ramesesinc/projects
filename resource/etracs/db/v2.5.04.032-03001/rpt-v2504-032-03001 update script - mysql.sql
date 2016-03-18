/* v2.5.04.032-03001 */
alter table cancelledfaas add `online` int default 0;
alter table cancelledfaas add `lguid` varchar(50);
alter table cancelledfaas add `lasttaxyear` int default 0;
alter table cancelledfaas modify column `remarks` text null;