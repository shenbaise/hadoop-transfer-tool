/**
 * 
 */
package org.sbs.transfer.schedule;

import static org.quartz.JobBuilder.newJob;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.google.inject.Guice;

/**
 * @author zhaoziqiang
 *
 */
public class PrintScheduler {
	public String cron = "10 * * * * ? *";
	public String name;
	public static Options options = new Options();
	static {
		options.addOption("n", "name", true, "name of job");
		options.addOption("c", "cron", true, "the cron string");
		options.addOption("h", "help", false, "get help");
		options.addOption("a", "add", false, "add a new job");
	}
	
	public boolean parse(String[] args){
		CommandLineParser parser = Guice.createInjector().getInstance(GnuParser.class);
		CommandLine commandLine;
		try {
			commandLine = parser.parse(options, args);
			if (commandLine.hasOption('h')) {
				HelpFormatter helpFormatter = new HelpFormatter();
				helpFormatter.printHelp("scheduler", options, true);
				return false;
			}
			if (commandLine.hasOption('a')) {
				add();
				return false;
			}
			if (!commandLine.hasOption("name")) {
				throw new ParseException("You must specify a name for scheduler --name");
			}
			name = commandLine.getOptionValue("name");
			if (!commandLine.hasOption("cron")) {
				throw new ParseException("You must specify a cron string for scheduler --cron");
			}
			cron = commandLine.getOptionValue("cron");
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return true ;
	}
	
	public void run(){
		try {
			CronScheduleBuilder schedBuilder = CronScheduleBuilder
					.cronSchedule(cron);
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, "TGroup")
					.withSchedule(schedBuilder).build();
			JobDetail job = newJob(PrintJob.class).withIdentity(name, "TGroup").withDescription("oh ,my god").build();
			_Scheduler.getScheduler().scheduleJob(job, trigger);
			_Scheduler.getScheduler().start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	public void add(){
		try {
			CronScheduleBuilder schedBuilder = CronScheduleBuilder
					.cronSchedule(cron);
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("new", "NGroup")
					.withSchedule(schedBuilder).build();
			JobDetail job = newJob(PrintJob.class).withIdentity(System.currentTimeMillis()+"newJob", "NGroup").withDescription("oh ,my god####").build();
			_Scheduler.getScheduler().scheduleJob(job, trigger);
			_Scheduler.getScheduler().start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		PrintScheduler ps = Guice.createInjector().getInstance(PrintScheduler.class);
		if(ps.parse(args)){
			ps.run();
		}
	}

}
