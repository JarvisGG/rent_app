package com.hc.xiaobairent.model;

public class ReckonModel {
	private String downpayment;// "13000元",
	private String instalment;// "10600元×6期"

	public String getDownpayment() {
		return downpayment;
	}

	public void setDownpayment(String downpayment) {
		this.downpayment = downpayment;
	}

	public String getInstalment() {
		return instalment;
	}

	public void setInstalment(String instalment) {
		this.instalment = instalment;
	}
}
