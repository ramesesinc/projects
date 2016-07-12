/* v2.5.04.032-03009 */

/*============================================
**
** RPT TRANSMITTAL UPDATES 
**
============================================*/
drop table rpttransmittal_item_data
go 
drop table rpttransmittal_item
go 
drop table rpttransmittal_log
go 
drop table rpttransmittal
go 


CREATE TABLE rpttransmittal (
  objid varchar(50) NOT NULL,
  state varchar(50) NOT NULL,
  type varchar(15) NOT NULL,
  filetype varchar(50) not null,
  txnno varchar(15) NOT NULL,
  txndate datetime NOT NULL,
  lgu_objid varchar(50) NOT NULL,
  lgu_name varchar(50) NOT NULL,
  lgu_type varchar(50) NOT NULL,
  tolgu_objid varchar(50) NOT NULL,
  tolgu_name varchar(50) NOT NULL,
  tolgu_type varchar(50) NOT NULL,
  createdby_objid varchar(50) NOT NULL,
  createdby_name varchar(100) NOT NULL,
  createdby_title varchar(50) NOT NULL,
  remarks varchar(500) default NULL,
  PRIMARY KEY  (objid)
)
go 

create unique index ux_txnno on rpttransmittal (txnno)
go 

create index ix_state on rpttransmittal(state)
go
create index ix_createdby_name on rpttransmittal(createdby_name)
go 
create index ix_lguname on rpttransmittal(lgu_name)
go 


CREATE TABLE rpttransmittal_item (
  objid varchar(50) NOT NULL,
  parentid varchar(50) NOT NULL,
  state varchar(50) NOT NULL,
  filetype varchar(50) NOT NULL,
  refid varchar(50) NOT NULL,
  refno varchar(150) NOT NULL,
  message varchar(350),
  remarks varchar(350),
  status varchar(50), 
  disapprovedby_name varchar(150),
  PRIMARY KEY  (objid)
)
go 

create unique index ux_parentid_refid on rpttransmittal_item(parentid,refid)
go 

create index ix_refid on rpttransmittal_item(refid)
go 
create index fk_rpttransmittal_item_rpttransmittal on rpttransmittal_item(parentid) 
go 


alter table rpttransmittal_item 
add constraint fk_rpttransmittal_item_rpttransmittal 
foreign key (parentid) references rpttransmittal (objid)
go 


alter table rpt_changeinfo add redflagid varchar(50)
go 




/*=================================================================
*
* MACHINE TAXABILITY
* 
=================================================================*/

alter table machdetail add taxable int
go 
update machdetail set taxable = 1 where taxable is null
go 
