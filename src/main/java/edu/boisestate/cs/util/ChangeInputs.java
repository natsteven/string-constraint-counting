package edu.boisestate.cs.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * It takes two json files:
 * file1: constraint json file
 * file2: inverse solution json file
 * It creates a copy of file1, where
 * the concrete values of the symbolic nodes
 * are substituded with the values from
 * file 2
 * @author elenasherman
 *
 */
public class ChangeInputs {

	public static void main(String[] args) {
		String graphPath = "./graphs/inverse/inverse_case_01.json";
		//args[0];
		String solutionPath = "./graphs/inverse/sol/solutions_01.txt";
		//args[1];
		String benchFileName = "./graphs/inverse/sol/inverse_case_01.json";
		if(args.length == 3) {
			graphPath = args[0];
			solutionPath = args[1];
			benchFileName = args[2];
		}

		// create json object mapper
		ObjectMapper graphMapper = new ObjectMapper();
		
		// create json ojbect mapper for solutions
		ObjectMapper solutionMapper = new ObjectMapper();
		File solutionFile = new File(solutionPath);
		Map<String,Object> solutionData = null;
		
		try {
			solutionData = solutionMapper.readValue(solutionFile, Map.class);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// init json file object
		File graphFile = new File(graphPath);
		
		Map<String, Object> graphData = null;
		try {
			graphData = graphMapper.readValue(graphFile, Map.class);
			// get constraint data from graph data
			List<Map<String, Object>> vertexData = (List<Map<String, Object>>) graphData.get("vertices");
			List<Map<String,Object>> inputsData = (List<Map<String, Object>>) solutionData.get("inputs");
			for (Map<String, Object> obj : vertexData) {
				// get constraint vertex data
				int id = (Integer) obj.get("id");
				String actualValue = (String) obj.get("actualValue");
				//find the same id in solutionData
				System.out.println("id " + id + ": " + actualValue);
				for(Map<String,Object> input : inputsData ) {
					int inputId = (Integer) input.get("ID");
					if(id == inputId) {
						String inputValue = (String) input.get("input");
						System.out.println("inId " + inputId + ": " + inputValue);
						obj.put("actualValue", inputValue);
						
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String json;
		try {
			json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(graphData);
			System.out.println(json);
			
			Writer benchWriter = new FileWriter(benchFileName);
			benchWriter.write(json.toString());
			benchWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
