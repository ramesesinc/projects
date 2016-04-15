/* v2.5.04.032-03005 */

alter table rpt_changeinfo add refid varchar(50);

update rpt_changeinfo set refid = faasid where refid is null;
	