/**
 * 
 */
package org.sbs.transfer.schedule;

import static org.quartz.JobBuilder.newJob;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author zhaoziqiang
 * transfer schedule
 */
public class TransferScheduler {
	
	public Scheduler scheduler;
	public String cron = "10 * * * * ? *";
	
	// 可以使用多个cron
	
	public void run() throws Exception {
		SchedulerFactory sf = new StdSchedulerFactory();
		scheduler = sf.getScheduler();
		CronScheduleBuilder schedBuilder = CronScheduleBuilder
				.cronSchedule(cron);
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("test")
				.withSchedule(schedBuilder).build();
		JobDetail job = newJob(TransferJob.class).build();
		scheduler.scheduleJob(job, trigger);
		scheduler.start();
	}
	
	public void stop() throws Exception{
		scheduler.shutdown(true);
	}
	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}
	
	public static void main(String[] args) {
		
		TransferScheduler scheduler = new TransferScheduler();
		try {
			scheduler.run();
			SchedulerQueen.sq.put("s1", scheduler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
