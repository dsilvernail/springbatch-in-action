/**
 * 
 */
package com.manning.sbia.ch03;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Launches the import invoices job.
 * WARNING: the job repository is emptied first!
 * @author acogoluegnes
 *
 */
public class LaunchImportInvoicesJob {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
			"/import-invoices-job-context.xml",
			"/com/manning/sbia/ch03/batch-infrastructure-pgsql-context.xml"
		);
		
		JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
		Job job = ctx.getBean(Job.class);	
		
		jobLauncher.run(job, new JobParametersBuilder()
			.addString("inputResource", "file:./invoices.zip")
			.addString("targetDirectory", "./importinvoicesbatch/")
			.addString("targetFile","invoices.txt")
			.addString("date", "2010-05-04")
			.toJobParameters()
		);

	}

}