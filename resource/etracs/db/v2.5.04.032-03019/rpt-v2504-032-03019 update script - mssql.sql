/* 254032-03019 */

/*==================================================
*
*  CDU RATING SUPPORT 
*
=====================================================*/

alter table bldgrpu add cdurating varchar(15)
go 

alter table bldgtype add usecdu int
go 
update bldgtype set usecdu = 0 where usecdu is null
go 


alter table bldgtype_depreciation add excellent decimal(16,2)
go 
alter table bldgtype_depreciation add verygood decimal(16,2)
go 
alter table bldgtype_depreciation add good decimal(16,2)
go 
alter table bldgtype_depreciation add average decimal(16,2)
go 
alter table bldgtype_depreciation add fair decimal(16,2)
go 
alter table bldgtype_depreciation add poor decimal(16,2)
go 
alter table bldgtype_depreciation add verypoor decimal(16,2)
go 
alter table bldgtype_depreciation add unsound decimal(16,2)
go 