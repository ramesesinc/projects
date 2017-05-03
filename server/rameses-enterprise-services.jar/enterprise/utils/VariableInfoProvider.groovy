package enterprise.utils;

import enterprise.facts.*;

public class VariableInfoProvider {


	String schemaName;

	public def createFact(dd) {
		def cf = null;	
		if(dd.datatype == "integer") {
			if(!createIntegerFact) 
				throw new Exception("createIntegerFact not implemented");
			cf = createIntegerFact();
		}
		else if(dd.datatype == "decimal") {
			if(!createDecimalFact) 
				throw new Exception("createDecimalFact not implemented");
			cf = createDecimalFact();
		}
		else if(dd.datatype == "boolean") {
			if(!createBooleanFact) 
				throw new Exception("createBooleanFact not implemented");
			cf = createBooleanFact();
		}
		else if(dd.datatype == "string") {
			if(!createStringFact) 
				throw new Exception("createStringFact not implemented");
			cf = createStringFact();
		}
		else if(dd.datatype == "date") {
			if(!createDateFact) 
				throw new Exception("createDateFact not implemented");
			cf = createDateFact();
		}
		else {
			if(!createObjectFact) 
				throw new Exception("createObjectFact not implemented");
			cf = createObjectFact();
		}
		
		//copy the data of the fact;
		cf.copy(dd);
		return cf;
	};

	def createDecimalFact;
	def createIntegerFact;
	def createBooleanFact;
	def createStringFact;
	def createDateFact;
	def createObjectFact;

}