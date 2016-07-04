package com.sap.sdc.hcp.bootcamp.doa;

import java.awt.List;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.DateTime;

import com.sap.sdc.hcp.bootcamp.model.Job;

public class JobDOAImp implements JobDOA {

	@Override
	public Collection<Job> getAllJobs() {
		// TODO Auto-generated method stub
		Collection<Job> jobs = new ArrayList<Job>();
		Job job2 = new Job();
		job2.setJOBID("JB2032");
		job2.setDEPARTMENT("Oracle");
		job2.setDESCRIPTION("Oracle Apps Consultant");
		jobs.add(job2);
		return jobs;
	}

	@Override
	public Job getJob(String Id) {
		// TODO Auto-generated method stub
		Job job2 = new Job();
		job2.setJOBID("JB2032");
		job2.setDEPARTMENT("Oracle");
		job2.setDESCRIPTION("Oracle Apps Consultant");
		return job2;
	}

	@Override
	public Job addNewJob(Job job) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteJob(String Id) {
		// TODO Auto-generated method stub

	}

}
