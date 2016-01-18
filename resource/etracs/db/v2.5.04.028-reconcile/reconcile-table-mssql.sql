
/****** Object:  Table [dbo].[entity_reconciled]    Script Date: 9/18/2015 5:18:05 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[entity_reconciled](
	[objid] [varchar](50) NOT NULL,
	[info] [text] NULL,
	[masterid] [varchar](50) NULL,
 CONSTRAINT [PK_entity_cancelled] PRIMARY KEY CLUSTERED 
(
	[objid] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

ALTER TABLE [dbo].[entity_reconciled]  WITH CHECK ADD  CONSTRAINT [FK_entity_reconciled_entity] FOREIGN KEY([masterid])
REFERENCES [dbo].[entity] ([objid])
GO

ALTER TABLE [dbo].[entity_reconciled] CHECK CONSTRAINT [FK_entity_reconciled_entity]
GO


/****** Object:  Table [dbo].[entity_reconciled_txn]    Script Date: 9/18/2015 5:21:19 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[entity_reconciled_txn](
	[objid] [varchar](50) NOT NULL,
	[reftype] [varchar](50) NOT NULL,
	[refid] [varchar](50) NOT NULL,
	[tag] [nchar](10) NULL,
 CONSTRAINT [PK_entity_reconciled_txn] PRIMARY KEY CLUSTERED 
(
	[objid] ASC,
	[reftype] ASC,
	[refid] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO




