package org.tess.automation.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.tess.automation.dao.AutomataComponent;
import org.tess.automation.dao.ProjectNode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONHandler {

	private static JSONHandler obj = null;

	private JSONHandler() {
	}

	public static JSONHandler getInstance() {
		if (obj == null) {
			obj = new JSONHandler();
		}
		return obj;
	}
	
	public static void main(String[] arg) {
		JSONHandler js = new JSONHandler();
		ProjectNode aa = js.readJSONFile("meta\\nodes.json");
		System.out.println();
	}

	public ProjectNode readJSONFile(String filePath) {
		ObjectMapper objectMapper = new ObjectMapper();

		URL res = getClass().getClassLoader().getResource(filePath);
		File file = null;
		try {
			file = Paths.get(res.toURI()).toFile();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ProjectNode json = null;
		try {
			json = objectMapper.readValue(file, ProjectNode.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}

}
