/*==================================
** V2.5.04.028
==================================*/

alter table bldgadditionalitem add addareatobldgtotalarea int;

update bldgadditionalitem set addareatobldgtotalarea = 0 where addareatobldgtotalarea is null;

alter table faasannotation modify column txnno varchar(15) not null;

