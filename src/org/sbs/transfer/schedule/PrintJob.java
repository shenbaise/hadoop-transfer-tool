/**
 * 
 */
package org.sbs.transfer.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;

/**
 * @author zhaoziqiang
 *
 */
public class PrintJob implements Job {
	
	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		System.out.println(arg0.hashCode() + "\t:\t" + System.currentTimeMillis());
		
		System.out.println("description:"+arg0.getJobDetail().getDescription());
		try {
			System.out.println("name:"+arg0.getScheduler().getSchedulerName());
			System.out.println("======"+arg0.getScheduler().checkExists(new JobKey("test", "TGroup")));
			GroupMatcher<JobKey> gm = GroupMatcher.jobGroupEquals("TGroup");
			System.out.println("++++++"+arg0.getScheduler().getJobKeys(gm));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
