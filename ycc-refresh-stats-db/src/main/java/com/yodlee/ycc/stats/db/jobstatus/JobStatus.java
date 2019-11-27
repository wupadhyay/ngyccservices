/**
 * Copyright (c) 2017 Yodlee Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.
 * Use is subject to license terms.
 *
 */
package com.yodlee.ycc.stats.db.jobstatus;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "job_status")
public class JobStatus {

	@Id
	private ObjectId id;
	private long job_id;
	private String job_name;
	private String collection_name;
	private Date start_date;
	private Date end_date;
	private String job_status;
	private long no_of_records;
	public ObjectId getId() {
		return id;
	}
	public long getJob_id() {
		return job_id;
	}
	public void setJob_id(long job_id) {
		this.job_id = job_id;
	}
	public String getJob_name() {
		return job_name;
	}
	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public String getJob_status() {
		return job_status;
	}
	public void setJob_status(String job_status) {
		this.job_status = job_status;
	}
	public long getNo_of_records() {
		return no_of_records;
	}
	public void setNo_of_records(long no_of_records) {
		this.no_of_records = no_of_records;
	}
	public String getCollection_name() {
		return collection_name;
	}
	public void setCollection_name(String collection_name) {
		this.collection_name = collection_name;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JobStatus [id=");
		builder.append(id);
		builder.append(", job_id=");
		builder.append(job_id);
		builder.append(", job_name=");
		builder.append(job_name);
		builder.append(", start_date=");
		builder.append(start_date);
		builder.append(", end_date=");
		builder.append(end_date);
		builder.append(", job_status=");
		builder.append(job_status);
		builder.append(", no_of_records=");
		builder.append(no_of_records);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
	
}
