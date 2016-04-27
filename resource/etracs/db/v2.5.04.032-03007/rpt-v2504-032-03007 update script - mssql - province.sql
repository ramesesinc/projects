/* v2.5.04.032-03007 */
/**********************************************************
** ufn_getDropDefaultConstraintStatement
**********************************************************/
IF OBJECT_ID (N'dbo.ufn_getDropDefaultConstraintStatement', N'FN') IS NOT NULL
    DROP FUNCTION dbo.ufn_getDropDefaultConstraintStatement
GO

CREATE FUNCTION dbo.ufn_getDropDefaultConstraintStatement (@table_name varchar(100), @col_name varchar(100))
RETURNS varchar(1000)
WITH EXECUTE AS CALLER
AS
BEGIN
  declare @Command  nvarchar(1000)

  select @Command = 'ALTER TABLE ' + @table_name + ' drop constraint ' + d.name
   from sys.tables t   
    join    sys.default_constraints d       
     on d.parent_object_id = t.object_id  
    join    sys.columns c      
     on c.object_id = t.object_id      
      and c.column_id = d.parent_column_id
   where t.name = @table_name
    and c.name = @col_name
  return @command 
END
GO



/**********************************************************
** ufn_getForeignKeyDropStatement
**********************************************************/

IF OBJECT_ID (N'dbo.ufn_getForeignKeyDropStatement', N'FN') IS NOT NULL
    DROP FUNCTION dbo.ufn_getForeignKeyDropStatement
GO

CREATE FUNCTION dbo.ufn_getForeignKeyDropStatement(@table_name varchar(100), @col_name varchar(100)
)
RETURNS varchar(1000)
WITH EXECUTE AS CALLER
AS
BEGIN
	declare @command  nvarchar(1000)
	
	select @command = 'ALTER TABLE ' + @table_name + ' DROP CONSTRAINT ' + fk.name
	from sys.foreign_keys  fk, sys.foreign_key_columns fkc
	where fk.object_id = fkc.constraint_object_id
	 and fk.parent_object_id = OBJECT_ID(@table_name) 
	 and COL_NAME(fkc.parent_object_id, fkc.parent_column_id) = @col_name
	 
	return @command 
END
GO

declare @command varchar(1000)

set @command = dbo.ufn_getDropDefaultConstraintStatement('subdivisionaffectedrpu', 'prevfaasid')
if @command is not null execute(@command)

set @command = dbo.ufn_getForeignKeyDropStatement('subdivisionaffectedrpu', 'prevfaasid')
if @command is not null execute(@command)
GO 


alter table subdivisionaffectedrpu alter column prevfaasid varchar(50) null
GO 

alter table subdivisionaffectedrpu alter column prevtdno varchar(50) null
GO 

alter table subdivisionaffectedrpu alter column prevpin varchar(50) null
go 
