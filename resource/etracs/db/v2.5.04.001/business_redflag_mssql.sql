
CREATE TABLE [dbo].[business_redflag](
[objid] [varchar](50) NOT NULL,
[businessid] [varchar](50) NULL,
[message] [varchar](255) NULL,
[dtfiled] [datetime] NULL,
[filedby_objid] [varchar](50) NULL,
[filedby_name] [varchar](255) NULL,
[resolved] [int] NULL,
[remarks] [varchar](255) NULL,
[blockaction] [varchar](50) NULL,
[effectivedate] [date] NULL,
[resolvedby_objid] [varchar](50) NULL,
[resolvedby_name] [varchar](100) NULL,
[caseno] [varchar](50) NULL 
) 
GO 
ALTER TABLE [dbo].[business_redflag] ADD PRIMARY KEY (objid) 
GO 
CREATE INDEX ix_businessid ON [dbo].[business_redflag] (businessid) 
GO 
CREATE INDEX ix_dtfiled ON [dbo].[business_redflag] (dtfiled) 
GO 
CREATE INDEX ix_filedby_objid ON [dbo].[business_redflag] (filedby_objid) 
GO 
CREATE INDEX ix_effectivedate ON [dbo].[business_redflag] (effectivedate) 
GO 
CREATE INDEX ix_resolvedby_objid ON [dbo].[business_redflag] (resolvedby_objid) 
GO 
