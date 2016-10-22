[getList]
SELECT r.*, 'POSTED' AS state FROM remoteserverdata r ORDER BY r.objid 

[getCollectionTypes]
SELECT * FROM collectiontype where org_objid=$P{orgid} ORDER BY formno, name 

[getCollectionTypeAccounts]
select ca.* from collectiontype c 
  inner join collectiontype_account ca on ca.collectiontypeid  = c.objid 
where c.org_objid=$P{orgid}   

[getCollectionGroups]
select * from collectiongroup where org_objid=$P{orgid} ORDER BY name 

[getCollectionGroupItems]
select b.* from collectiongroup a 
  inner join collectiongroup_revenueitem b on a.objid = b.collectiongroupid 
where a.org_objid=$P{orgid} 

[getAF]
select distinct * 
from ( 
  select af.* from collectiontype ct 
    inner join af on ct.formno=af.objid  
  where ct.org_objid=$P{orgid} 
  union 
  select af.* from sys_usergroup_member ugm 
    inner join af_control afc on ugm.user_objid=afc.owner_objid 
    inner join af on afc.afid=af.objid 
  where ugm.org_objid=$P{orgid}  
  union 
  select af.* from af_control afc 
    inner join af on afc.afid=af.objid 
  where afc.org_objid=$P{orgid}  
)xx 

[getFunds]
select distinct * 
from ( 
  select f.* 
  from ( select objid from collectiontype where org_objid=$P{orgid} )xx 
    inner join collectiontype_account ca on xx.objid = ca.collectiontypeid 
    inner join itemaccount ia on ca.account_objid = ia.objid 
    inner join fund f on ia.fund_objid = f.objid 
  union 
  select f.* from itemaccount ia 
    inner join fund f on ia.fund_objid=f.objid 
  where ia.org_objid=$P{orgid} 
)xx 

[getItemAccounts]
select distinct * 
from ( 
  select ia.* 
  from ( select objid from collectiontype where org_objid=$P{orgid} )xx 
    inner join collectiontype_account ca on xx.objid = ca.collectiontypeid 
    inner join itemaccount ia on ca.account_objid = ia.objid 
    inner join fund f on ia.fund_objid = f.objid 
  union 
  select ia.* from itemaccount ia 
    inner join fund on ia.fund_objid=fund.objid 
  where ia.org_objid=$P{orgid} 
  union 
  select ia.* from collectiongroup a 
    inner join collectiongroup_revenueitem b on a.objid = b.collectiongroupid 
    inner join itemaccount ia on b.revenueitemid=ia.objid 
    inner join fund on ia.fund_objid=fund.objid 
  where a.org_objid=$P{orgid} 
 )xx 

[getUserGroups]
select xx.* from ( 
  select su.* from sys_usergroup_member sm  
    inner join sys_usergroup su on sm.usergroup_objid = su.objid 
  where sm.org_objid = $P{orgid} 
  union 
  select * from sys_usergroup 
  where objid = 'TREASURY.LIQUIDATING_OFFICER' 
)xx 

[getUser]
select u.* from (
  select su.objid from sys_usergroup_member sm 
    inner join sys_user su on sm.user_objid = su.objid 
  where sm.org_objid = $P{orgid} 
  union 
  select su.objid from sys_usergroup_member sm 
    inner join sys_user su on sm.user_objid = su.objid 
  where sm.usergroup_objid='TREASURY.LIQUIDATING_OFFICER'
)xx inner join sys_user u on xx.objid = u.objid 

[getUserMemberships]
select um.* from ( 
  select objid from sys_usergroup_member 
  where org_objid = $P{orgid} 
  union 
  select objid from sys_usergroup_member 
  where usergroup_objid = 'TREASURY.LIQUIDATING_OFFICER'  
)xx inner join sys_usergroup_member um on xx.objid = um.objid 

[getUserCashBooks]
select * from cashbook 
where subacct_objid in ( 
    select distinct su.objid from sys_usergroup_member sm 
      inner join sys_user su on sm.user_objid = su.objid 
    where sm.org_objid = $P{orgid} 
  ) and fund_objid in ( 
    select distinct f.objid from collectiontype c 
      inner join collectiontype_account ca on ca.collectiontypeid  = c.objid 
      inner join itemaccount ia on ia.objid = ca.account_objid 
      inner join fund f on f.objid = ia.fund_objid 
    where c.org_objid = $P{orgid} 
  )

[getOrgs]
select distinct * 
from ( 
  select * from sys_org where root=1 
  union 
  select * from sys_org where objid in (  
    select parent_objid from sys_org where objid=$P{orgid} 
  )
  union 
  select * from sys_org where objid=$P{orgid} 
)xx 

[getOrgClasses]
select * from sys_orgclass 

[getCashBooksDetail]
select * from cashbook_entry where parentid=$P{objid} 

[insertUserMembership]
INSERT INTO sys_usergroup_member(
   objid,
   state,
   usergroup_objid,
   user_objid,
   user_username,
   user_firstname,
   user_lastname,
   org_objid,
   org_name,
   org_orgclass,
   securitygroup_objid,
   exclude,
   displayname,
   jobtitle)
VALUES (
   $P{objid},
   $P{state},
   $P{usergroup_objid},
   $P{user_objid},
   $P{user_username},
   $P{user_firstname},
   $P{user_lastname},
   $P{org_objid},
   $P{org_name},
   $P{org_orgclass},
   $P{securitygroup_objid},
   $P{exclude},
   $P{displayname},
   $P{jobtitle} 
)


[insertFund]
INSERT INTO fund(
   objid
  ,parentid
  ,state
  ,code
  ,title
  ,type
  ,special
)
VALUES (
  $P{objid}
  ,$P{parentid}
  ,$P{state}
  ,$P{code}
  ,$P{title}
  ,$P{type}
  ,$P{special}
)


[insertSpecialAccountSetting]
INSERT INTO specialaccountsetting
  (objid,
   item_objid,
   amount,
   collectiontypeid,
   revtype)
VALUES
(
  $P{objid},
  $P{item_objid},
  $P{amount},
  $P{collectiontypeid},
  $P{revtype} 
) 