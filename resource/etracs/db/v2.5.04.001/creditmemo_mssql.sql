
CREATE TABLE creditmemotype (
[objid] [varchar](50) NOT NULL,
[title] [varchar](50) NULL,
[issuereceipt] [int] NULL,
[handler] [varchar](50) NULL,
[sortorder] [int] NULL
) 
GO 
ALTER TABLE creditmemotype ADD PRIMARY KEY (objid)
GO 

CREATE TABLE creditmemotype_account (
[typeid] [varchar](50) NOT NULL,
[account_objid] [varchar](50) NOT NULL,
[account_title] [varchar](100) NULL,
[tag] [varchar](50) NULL,
[sortorder] [int] NULL 
)
GO
ALTER TABLE creditmemotype_account ADD PRIMARY KEY (typeid,account_objid) 
GO 

CREATE TABLE creditmemo (
	[objid] [varchar](50) NOT NULL,
	[state] [varchar](25) NOT NULL,
	[refdate] [date] NOT NULL,
	[refno] [varchar](25) NOT NULL,
	[amount] [numeric](16, 2) NOT NULL,
	[particulars] [varchar](250) NULL,
	[type_objid] [varchar](50) NULL,
	[type_title] [varchar](150) NULL,
	[bankaccount_objid] [varchar](50) NULL,
	[bankaccount_title] [varchar](150) NULL,
	[payer_objid] [varchar](50) NULL,
	[payername] [varchar](150) NULL,
	[payeraddress] [varchar](255) NULL,
	[controlno] [varchar](50) NULL,
	[receiptid] [varchar](50) NULL,
	[receiptno] [varchar](50) NULL,
	[dtissued] [date] NULL,
	[issuedby_objid] [varchar](50) NULL,
	[issuedby_name] [varchar](150) NULL
) 
GO
ALTER TABLE creditmemo ADD PRIMARY KEY (objid)
GO 

CREATE TABLE creditmemoitem ( 
	[objid] [varchar](50) NOT NULL,
	[parentid] [varchar](50) NOT NULL,
	[item_objid] [varchar](50) NOT NULL,
	[amount] [numeric](16, 2) NOT NULL 
) 
GO 
ALTER TABLE creditmemoitem ADD PRIMARY KEY (objid) 
GO 
ALTER TABLE creditmemoitem 
	ADD CONSTRAINT creditmemoitem_ibfk_1 
	FOREIGN KEY (parentid) REFERENCES creditmemo (objid) 
GO 
ALTER TABLE creditmemoitem 
	ADD CONSTRAINT creditmemoitem_ibfk_2 
	FOREIGN KEY (item_objid) REFERENCES itemaccount (objid) 
GO
ALTER TABLE creditmemoitem 
	ADD CONSTRAINT FK_creditmemo_item 
	FOREIGN KEY(parentid) REFERENCES creditmemo (objid)
GO
ALTER TABLE creditmemoitem 
	ADD CONSTRAINT FK_creditmemoitem_revenueitem 
	FOREIGN KEY(item_objid) REFERENCES itemaccount (objid)
GO

ALTER TABLE creditmemotype ADD fund_objid varchar(50) NULL
;
ALTER TABLE creditmemotype ADD fund_code varchar(50) NULL
;
ALTER TABLE creditmemotype ADD fund_title varchar(255) NULL
;
