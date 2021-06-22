/**
 * 
 */
package edu.boisestate.cs.graph;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author marli
 *
 */
public class SPFInputSet {
	
	public boolean SAT;
	
	@JsonProperty("inputs")
	public List<SPFInput> inputSet = new ArrayList<SPFInput>();
	
}
