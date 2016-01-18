package loan.facts;

import java.util.*;

public class SegregationType
{
	String objid;
	String name;
	String description;

	public SegregationType( o ) {
		objid = o.objid;
		name = o.name;
		description = o.description;
	}

	def toMap() {
		return [
			objid		: objid,
			name		: name,
			description	: description
		];
	}
}