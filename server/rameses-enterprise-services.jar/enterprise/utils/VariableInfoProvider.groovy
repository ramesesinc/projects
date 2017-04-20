package enterprise.utils;

import enterprise.facts.*;

public class VariableInfoProvider {

	String schemaName;
	def createFact = { dd->
		if(dd.datatype == "integer") {
			if(!createIntegerFact) 
				throw new Exception("createIntegerFact not implemented");
			return createIntegerFact(dd);
		}
		else if(dd.datatype == "decimal") {
			if(!createDecimalFact) 
				throw new Exception("createDecimalFact not implemented");
			return createDecimalFact(dd);
		}
		else if(dd.datatype == "boolean") {
			if(!createBooleanFact) 
				throw new Exception("createBooleanFact not implemented");
			return createBooleanFact(dd);
		}
		else if(dd.datatype == "string") {
			if(!createStringFact) 
				throw new Exception("createStringFact not implemented");
			return createStringFact(dd);
		}
		else if(dd.datatype == "date") {
			if(!createDateFact) 
				throw new Exception("createDateFact not implemented");
			return createDateFact(dd);
		}
		else {
			if(!createObjectFact) 
				throw new Exception("createObjectFact not implemented");
			return createObjectFact(dd);
		}
	};

	def createDecimalFact;
	def createIntegerFact;
	def createBooleanFact;
	def createStringFact;
	def createDateFact;
	def createObjectFact;

}