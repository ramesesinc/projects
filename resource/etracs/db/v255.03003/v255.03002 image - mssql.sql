/* 255-03003 */

-- NOTE: SELECT IMAGE DATABASE 
alter table image_header add parentid varchar(50)
go 
create index ix_imageheader_parentid on image_header(parentid)
go 

