package edu.sjsu.cmpe275.lab2.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.thoughtworks.xstream.XStream;

public class ControllerUtil {

	public static ResponseEntity<?> sendBadRequest(String errMsg, HttpStatus status) {
		BadRequest badRequest = new BadRequest();
		badRequest.setCode(status.toString());
		badRequest.setMsg(errMsg);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<BadRequest>(badRequest, responseHeaders, status);
	}
	
	public static ResponseEntity<?> sendSuccess(String errMsg) {
		Response request = new Response();
		HttpStatus status = HttpStatus.OK;
		request.setCode(status.toString());
		request.setMsg(errMsg);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_XML);
		
		XStream xs = new XStream();
		xs.alias("Response", Response.class);
		return new ResponseEntity<String>(xs.toXML(request), responseHeaders, status);
	}
}
