package com.sap.sdc.hcp.bootcamp.doa;

import java.util.Collection;

import com.sap.sdc.hcp.bootcamp.model.Job;

public interface JobDOA {

	public Collection<Job> getAllJobs();
	public Job getJob(String Id);
	public Job addNewJob(Job job);
	public void deleteJob(String Id); 
}
