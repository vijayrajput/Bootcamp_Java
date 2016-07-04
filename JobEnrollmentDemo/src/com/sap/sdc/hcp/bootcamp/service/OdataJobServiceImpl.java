package com.sap.sdc.hcp.bootcamp.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.sap.sdc.hcp.bootcamp.doa.JobDOA;
import com.sap.sdc.hcp.bootcamp.doa.JobDOAImp;
import com.sap.sdc.hcp.bootcamp.model.Job;
import com.sap.sdc.hcp.bootcamp.odata.model.Ojob;

public class OdataJobServiceImpl implements OdataJobService {

private JobDOA jobdoa;

	public OdataJobServiceImpl() {
		jobdoa = new JobDOAImp();
	}

	@Override
	public List<Ojob> getJobEntitySet() {
		// TODO Auto-generated method stub
		List <Ojob> ojobs = new ArrayList<Ojob>();
		Collection<Job> jobs = jobdoa.getAllJobs();
		Iterator<Job> iterator = jobs.iterator();
		while(iterator.hasNext()) {
			Job job = iterator.next();
			Ojob ojob = new Ojob();
			ojob.setJobID(job.getJOBID());
			ojob.setDescription(job.getDESCRIPTION());
			ojob.setDeparment(job.getDEPARTMENT());
			ojob.setDaysLeft(3);
			ojobs.add(ojob);
	      }
		
		return ojobs;
	}

	@Override
	public Ojob getJobEntity(String Id) {
		// TODO Auto-generated method stub
		Job job = jobdoa.getJob(Id);
		Ojob ojob = new Ojob();
		ojob.setJobID(job.getJOBID());
		ojob.setDescription(job.getDESCRIPTION());
		ojob.setDeparment(job.getDEPARTMENT());
		ojob.setDaysLeft(3);
		return ojob;
	}

}
