package edu.boisestate.cs.graph;

import java.util.Map;

/**
 * Enum that represents the solver operation for a constraint. These are set by the parser 
 * during the forward pass through the graph. This is used during the inverse pass in
 * order to avoid parsing the constraint a second time. The arguments listed here are 
 * stored by the parser inside the constraint during the forward pass. This lets the
 * inverse solver reuse arguments without having to parse the constraint to determine what
 * they were.
 * 
 * @author Marlin Roberts
 *
 */
public enum Operation {	CONCAT,						// args:
						CONCAT_CON, 				// args: 0:CSID
						CONCAT_SYM,					// args: 0:SSID
						CONCAT_SYM_PREF,			// args: 0:SSID
						CONCAT_SYM_SUFF,			// args: 0:SSID
						CONCAT_CON_START_END,		// args: 0:CSID, 1:START, 2:END
						CONCAT_SYM_START_END,		// args: 0:SSID, 1:START, 2:END
						DELETE,						// args:
						DELETE_START_END,			// args: 0:START, 1:END
						DELETE_CHAR_AT,				// args: 0:LOCATION
						EQUALS,						// args: 
						INSERT,						// args:
						REPLACE,					// args:
						REPLACE_CHAR_CHAR,			// args: 0:FIND, 1:REPLACE
						REPLACE_UNK_CHAR,			// args: 0:REPLACE
						REPLACE_CHAR_UNK,			// args: 0:FIND
						REPLACE_FIRST,				// args: 0:FIND, 1:REPLACE
						REPLACE_ALL,				// args: 0:FIND, 1:REPLACE
						REPLACE_UNK_UNK,			// args: 
						REPLACE_CHARSEQ_CHARSEQ,	// args: 0:FIND, 1:REPLACE
						REVERSE,					// args:
						SET_CHAR_AT,				// args:
						SET_LENGTH,					// args:
						SUBSTRING,					// args:
						SUBSTR_STRT_END,		// args: 0:START, 1:END
						SUBSTRING_START,			// args: 0:START
						TOLOWERCASE,				// args: 
						TOUPPERCASE,				// args: 
						TRIM,						// args: 
						INIT,						// args: 
						INIT_CON,					// args: 
						INIT_SYM,					// args: 
						PREDICATE,					// args: 
						PROPAGATION,				// args: 
						TERMINAL,					// args: 
						INPUT,					// args: 
						UNDEFINED;					// args: 

	
	/**
	 * Static method to determine the correct operation enum for a constraint.
	 * This should ultimately replace most of the parser logic.
	 * 
	 * Some information may not be available within the constraint, such as whether 
	 * an argument is concrete or symbolic. The logic can return the general type in 
	 * these cases.
	 * 
	 * @param constraint - constraint to parse
	 * @return Operation - the operation enum for the constraint
	 */
	public static Operation constraintOp (PrintConstraint constraint) {
		
	    // get constraint info as variables
		Map<String, Integer> sourceMap = 	constraint.getSourceMap();
        String splitVal = 					constraint.getSplitValue();
        String actualVal = 					constraint.getActualVal();
        String functionName =				splitVal.split("!!")[0];
        int id = 							constraint.getId();
        int sourceMapSize = 				sourceMap.size();
        
        if (functionName.startsWith("r") || functionName.startsWith("$r")) {
        	functionName = "<init>";
        }
		
        Operation returnOp = UNDEFINED;
        
		switch (functionName) {
		
			case "concatenate":
			case "concat":
			case "append":
			
				returnOp = CONCAT;
				
				break;
			
			case "<init>":
				
				returnOp = INIT;
				
				break;
			
			case "substring":
				
				returnOp = SUBSTRING;
				
				break;
			
			case "setLength":
				
				returnOp = SET_LENGTH;
				
				break;
			
			case "insert":
				
				returnOp = INSERT;
				
				break;
				
			case "setCharAt":
				
				returnOp = SET_CHAR_AT;
				
				break;
				
			case "trim":
				
				returnOp = TRIM;
				
				break;
			
			case "delete":
				
				returnOp = DELETE;
				
				break;
			
			case "deleteCharAt":
				
				returnOp = DELETE_CHAR_AT;
				
				break;
				
			case "reverse":
				
				returnOp = REVERSE;
				
				break;
				
			case "replace":
				
				returnOp = REPLACE;
				
				break;
				
			case "replaceFirst":
				
				returnOp = REPLACE_FIRST;
				
				break;
				
			case "replaceAll":
				
				returnOp = REPLACE_ALL;
				
				break;
				
			case "toUpperCase":
				
				if (sourceMapSize == 1) { returnOp = TOUPPERCASE; }
				
				break;
			
			case "toLowerCase":
				
				if (sourceMapSize == 1) { returnOp = TOLOWERCASE; }
				
				break;
			
			case "toString":
			case "intern":
			case "trimToSize":
			case "length":
			case "charAt":
				
				returnOp = PROPAGATION;
				
				break;
				
			case "valueOf":
			case "copyValueOf":
				
				if (sourceMapSize == 2) { returnOp = PROPAGATION; }
				
				break;
		
		} // end switch
		
		return returnOp;
	
	} // end constraintOp

} // end Operation
