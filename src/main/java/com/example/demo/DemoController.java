package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.Jackson2Annotator;
import org.jsonschema2pojo.SchemaGenerator;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.SchemaStore;
import org.jsonschema2pojo.SourceType;
import org.jsonschema2pojo.rules.RuleFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Input;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.codemodel.JCodeModel;

@RestController
public class DemoController {

	@GetMapping("/get-Json-Data")
	public ResponseEntity<Input> getJsonResponse() throws JsonParseException, JsonMappingException, IOException {

		createPojo(); //creating pojo from json data

		Input inputJson = getJson(); //reading data from json file and returning pojo object

		return new ResponseEntity<Input>(inputJson, HttpStatus.OK);
	}

	public void createPojo() {
		String packageName = "com.example.demo.models";
		File inputJson = new File(
				"C:/Users/sbodra/Documents/workspace-sts-3.9.6.RELEASE/demo.zip_expanded/demo/src/main/resources/input.json");
		File outputPojoDirectory = new File(
				"C:/Users/sbodra/Documents/workspace-sts-3.9.6.RELEASE/demo.zip_expanded/demo/src/main/java");
		outputPojoDirectory.mkdirs();
		try {
			convert2JSON(inputJson.toURI().toURL(), outputPojoDirectory, packageName,
					inputJson.getName().replace(".json", ""));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Encountered issue while converting to pojo: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void convert2JSON(URL inputJson, File outputPojoDirectory, String packageName, String className)
			throws IOException {
		JCodeModel codeModel = new JCodeModel();
		URL source = inputJson;
		GenerationConfig config = new DefaultGenerationConfig() {
			@Override
			public boolean isGenerateBuilders() { // set config option by overriding method
				return true;
			}

			public SourceType getSourceType() {
				return SourceType.JSON;
			}
		};
		SchemaMapper mapper = new SchemaMapper(
				new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());
		mapper.generate(codeModel, className, packageName, source);
		codeModel.build(outputPojoDirectory);
	}
	
	public Input getJson() throws JsonParseException, JsonMappingException, IOException {
		Input inputJson = new Input();
		ObjectMapper mapper = new ObjectMapper();
		inputJson = mapper.readValue(new File(
				"C:/Users/sbodra/Documents/workspace-sts-3.9.6.RELEASE/demo.zip_expanded/demo/src/main/resources/input.json"),
				new TypeReference<Input>() {
				});
		return inputJson;
	}
}
