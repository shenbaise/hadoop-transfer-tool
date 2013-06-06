/**
 * 
 */
package org.sbs.transfer.schedule;

import java.util.Collection;
import java.util.Iterator;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

/**
 * @author zhaoziqiang
 *
 */
public class _Scheduler {
	public static SchedulerFactory sf = new StdSchedulerFactory();
	
	/**
	 * get Scheduler
	 * @return
	 */
	public static Scheduler getScheduler(){
		try {
			return sf.getScheduler();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		try {
			Collection<Scheduler> c = sf.getAllSchedulers();
			Iterator<Scheduler> it = c.iterator();
			while(it.hasNext()){
				Scheduler s = it.next();
				System.out.println("s name:"+s.getSchedulerName());
				GroupMatcher<JobKey> gm = GroupMatcher.jobGroupEquals("TGroup");
				System.out.println(s.getJobKeys(gm));
			}
			
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
}
