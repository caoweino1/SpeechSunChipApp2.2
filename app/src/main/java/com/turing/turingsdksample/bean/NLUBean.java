package com.turing.turingsdksample.bean;

public class NLUBean {

	/**
	 * input : 一加一等于几 output : 我努力算！2. fld : math action : ["math"] math :
	 * {"master1":"1","suf1":"add"} state : <s>
	 */

	private String input;
	private String output;
	private String fld;

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getFld() {
		return fld;
	}

	public void setFld(String fld) {
		this.fld = fld;
	}

}
