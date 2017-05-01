package edu.sjsu.cmpe275.lab2.controller;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

/**
 * 
 * @author Imran
 *
 */
@JsonTypeInfo(include=As.WRAPPER_OBJECT, use=JsonTypeInfo.Id.NAME)
public class Response implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String code;
	private String msg;

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
