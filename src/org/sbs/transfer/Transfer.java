/**
 * 
 */
package org.sbs.transfer;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sbs.transfer.ftp.FTPTransfer;
import org.sbs.transfer.hdfs.HDFSTransfer;
import org.sbs.transfer.utils.Constant;
import org.sbs.transfer.utils.Constant.CopyPolicy;
import org.sbs.transfer.utils.DateUtils;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * @author zhaoziqiang
 * 
 */
public class Transfer {

	private Log log = LogFactory.getLog(getClass());
	// src hdfs
	@Inject
	public String address = "";
	public String port = "";
	public String user = "";
	public String password = "";
	public String src = "";
	public boolean delete = false;
	public CopyPolicy option = CopyPolicy.OverWrite; 
	
	// dis hdfs , read from configure file.
	public String _address = "172.16.113.10";
	public String _port = "9000";
	public String _user = "opz";
	public String _password = "";
	public String dis = "";
	
	public String type = "";
	public ITransfer iTransfer;
	
	public ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		  	-H 192.168.70.100
			-F /zzq/你/好/Todo.hea
			-D /ttt.hea
			-U anonymous
			-P anonymous
			-t ftp
		 */
//		Transfer transfer = new Transfer();
		Injector injector =  Guice.createInjector();
		Transfer transfer = injector.getInstance(Transfer.class); 
		
		try {
			if(transfer.parseCommandLine(args)){
				transfer.log.info("start ->:	" + DateUtils.dateFormat.format(new Date()));
				transfer.run();
				transfer.log.info("end ->:	" + DateUtils.dateFormat.format(new Date()));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * parse command
	 * @param args
	 * @return
	 * @throws ParseException
	 */
	private boolean parseCommandLine(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption("H", "host", true, "hostname of the avro source")
				.addOption("p", "port", true, "port of the avro source")
				.addOption("F", "filename", true,"file or directory to transfer")
				.addOption("D", "destination", true,"destination directory")
				.addOption("U", "user", true,"user name")
				.addOption("P", "password", true,"password")
				.addOption("t", "type", true,"type of resources.\nsupport ftp,hdfs.[ftp|hdfs]")
				.addOption("d", "delete", true,"delete files after tansfer.[true|false]")
				.addOption("o", "option", true,"overwrite,append or ignore if file exist on hdfs,o for overwrite,a for append,i for ignore.[a|i|o]")
//				.addOption(null, "echo", true,"print file name transferring")
				.addOption(null, "list", true,"list all tasks")
				.addOption(null, "shutdown", true,"shutdown a task.[task ID]")
				.addOption(null, "start", true,"start a task [task ID]")
				.addOption(null, "echo", true,"print file name transferring")
				.addOption("h", "help", false, "get help infomation");
		
		// test guice ... actually，we can just write -> CommandLineParser parser = new GnuParser(); :)
		Injector injector =  Guice.createInjector(new Module() {
			@Override
			public void configure(Binder arg0) {
				arg0.bind(CommandLineParser.class).to(GnuParser.class);
			}
		});
		CommandLineParser parser = injector.getInstance(CommandLineParser.class);
		CommandLine commandLine = parser.parse(options, args);
		if (commandLine.hasOption('h')) {
			HelpFormatter helpFormatter = new HelpFormatter();
			helpFormatter.printHelp(60, "transfer", 
					"example 1: java -jar transfer.jar -H 192.168.*.* -F /zzq -D /ttt -U opz -P anonymous -t ftp" +
					"\nexample 2: java -jar transfer.jar -H 192.168.*.* -p 9000 -F /ttt -D /zzz -U opz -t hdfs\n", 
					options, "this tool is used to transfer files to c01's hdfs.\nit support ftp & hdfs.", true);
//			helpFormatter.printHelp("transfer", options, true);
			return false;
		}
		if (!commandLine.hasOption("host")) {
			throw new ParseException("You must specify a ip address to connect to with --host");
		}
		address = commandLine.getOptionValue("host");
		if (!commandLine.hasOption("filename")) {
			throw new ParseException("You must specify a source file or directory to transfer --filename");
		}
		src = commandLine.getOptionValue("filename");
		if(!commandLine.hasOption("destination")){
			throw new ParseException("You must specify a destination fileName or direcotry to stor --destination");
		}
		dis = commandLine.getOptionValue("destination");
		if(!commandLine.hasOption("type")){
			throw new ParseException("You must specify a transfer type --type");
		}
		type = commandLine.getOptionValue("type");
		if(commandLine.hasOption("port")){
			port = commandLine.getOptionValue("port");
		}
		if(commandLine.hasOption("user")){
			user = commandLine.getOptionValue("user");
		}
		if(commandLine.hasOption("password")){
			password = commandLine.getOptionValue("password");
		}
		if(commandLine.hasOption("delete")){
			delete = Boolean.valueOf(commandLine.getOptionValue("delte"));
		}
		if(commandLine.hasOption("option")){
			String o = commandLine.getOptionValue("option");
			if(StringUtils.isNotBlank(o)){
				if(Constant.APPEND.equals(o)){
					option = CopyPolicy.Append;
				}else if (Constant.IGNORE.equals(o)) {
					option = CopyPolicy.Ignore;
				}else if (Constant.OVERWRITE.equals(o)) {
					option = CopyPolicy.OverWrite;
				}else {
					throw new ParseException("you must specify a,i or o --option");
				}
					
			}
		}
		return true;
	}
	/**
	 * to run.
	 */
	private void run() throws Exception{
		if(Constant.transferFTPType.equals(type)){
			iTransfer = new FTPTransfer(address, port, user, password, _address, _port, _user, _password);
		}else if(Constant.transferHDFSType.equals(type)){
			iTransfer = new HDFSTransfer(address, port, user, password, _address, _port, _user, _password);
		}else {
			throw new Exception("not support.type -h for help.");
		}
		iTransfer.transfer(src, dis, delete,option);
	}
	
}

