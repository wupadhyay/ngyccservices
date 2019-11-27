package com.yodlee.ycc.dapi.task;

import com.yodlee.ycc.dapi.bean.KeyValue;

public class SplunkOutput {
	private KeyValue reportType;
	private String splunkResponse;
	private boolean failed;
	private StackTraceElement[] exceptionStacktrace;
	public KeyValue getReportType() {
		return reportType;
	}
	public void setReportType(KeyValue reportType) {
		this.reportType = reportType;
	}
	public String getSplunkResponse() {
		return splunkResponse;
	}
	public void setSplunkResponse(String splunkResponse) {
		this.splunkResponse = splunkResponse;
	}
	public boolean getFailed() {
		return failed;
	}
	public void setFailed(boolean failed) {
		this.failed = failed;
	}
	public StackTraceElement[] getExceptionStacktrace() {
		return exceptionStacktrace;
	}
	public void setExceptionStacktrace(StackTraceElement[] stackTraceElements) {
		this.exceptionStacktrace = stackTraceElements;
	}
	
	
}
