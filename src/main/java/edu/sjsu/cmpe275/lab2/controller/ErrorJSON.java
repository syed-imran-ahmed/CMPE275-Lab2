package edu.sjsu.cmpe275.lab2.controller;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 
 * @author Imran
 *
 */
public class ErrorJSON {

	private String errMsg; 
	
	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public ErrorJSON(String errMsg)
	{
		this.errMsg = errMsg;
	}
	
	public String getNotFoundError() throws JsonProcessingException
	{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.createObjectNode();
		JsonNode childNodes = mapper.createObjectNode();
		((ObjectNode) childNodes).put("code", HttpStatus.NOT_FOUND.toString());
		((ObjectNode) childNodes).put("msg", errMsg);
		((ObjectNode) rootNode).set("BadRequest", childNodes);
	
		return mapper.writeValueAsString(rootNode);
	}
	
	public String getBadRequestError() throws JsonProcessingException
	{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.createObjectNode();
		JsonNode childNodes = mapper.createObjectNode();
		((ObjectNode) childNodes).put("code", HttpStatus.BAD_REQUEST.toString());
		((ObjectNode) childNodes).put("msg", errMsg);
		((ObjectNode) rootNode).set("BadRequest", childNodes);
		
		return mapper.writeValueAsString(rootNode);
		
	}
	
	public String getSuccessfulMsg() throws JsonProcessingException
	{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.createObjectNode();
		JsonNode childNodes = mapper.createObjectNode();
		((ObjectNode) childNodes).put("code", HttpStatus.OK.toString());
		((ObjectNode) childNodes).put("msg", errMsg);
		((ObjectNode) rootNode).set("Response", childNodes);
		
		return mapper.writeValueAsString(rootNode);
	}
}
