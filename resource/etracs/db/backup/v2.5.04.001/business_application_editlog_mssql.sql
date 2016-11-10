
CREATE TABLE [dbo].[business_application_editlog](
[objid] [varchar](50) NOT NULL,
[businessid] [varchar](50) NOT NULL,
[applicationid] [varchar](50) NOT NULL,
[dtfiled] [datetime] NOT NULL,
[filedby_objid] [varchar](50) NOT NULL,
[filedby_name] [varchar](100) NOT NULL,
[amendtype] [varchar](50) NOT NULL,
[oldvalue] [varchar](255) NOT NULL,
[newvalue] [varchar](255) NOT NULL, 
[reason] [varchar](255) NOT NULL ,
[entryno] [varchar](50) NOT NULL 
) 
GO
CREATE INDEX ix_businessid ON [dbo].[business_application_editlog] (businessid)
GO 
CREATE INDEX ix_applicationid ON [dbo].[business_application_editlog] (applicationid)
GO 
CREATE INDEX ix_dtfiled ON [dbo].[business_application_editlog] (dtfiled)
GO 
CREATE INDEX ix_filedby_objid ON [dbo].[business_application_editlog] (filedby_objid)
GO 
CREATE INDEX ix_entryno ON [dbo].[business_application_editlog] (entryno)
GO 
ALTER TABLE [dbo].[business_application_editlog] ADD PRIMARY KEY (objid)
GO 
ALTER TABLE [dbo].[business_application_editlog]  
	ADD  CONSTRAINT [FK_business_application_editlog_business_application] 
	FOREIGN KEY([applicationid]) REFERENCES [dbo].[business_application] ([objid]) 
GO
