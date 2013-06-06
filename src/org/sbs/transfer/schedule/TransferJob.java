/**
 * 
 */
package org.sbs.transfer.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.sbs.transfer.ITransfer;
import org.sbs.transfer.excetption.TransferException;
import org.sbs.transfer.ftp.FTPTransfer;
import org.sbs.transfer.hdfs.HDFSTransfer;
import org.sbs.transfer.utils.Constant;
import org.sbs.transfer.utils.Constant.CopyPolicy;

import com.google.inject.Inject;


/**
 * @author zhaoziqiang
 *
 */
public class TransferJob implements Job {
	
	public ITransfer iTransfer;
	
	public String src = "/zzq";
	public String dis = "/ttt";
	public String type = "ftp";
	// src hdfs
	@Inject
	public String address = "192.168.70.100";
	public String port = "";
	public String user = "anonymous";
	public String password = "anonymous";
	public boolean delete = false;
	public CopyPolicy option = CopyPolicy.OverWrite;
	
	// dis hdfs , read from configure file.
	public String _address = "172.16.113.10";
	public String _port = "9000";
	public String _user = "opz";
	public String _password = "";
	
	public TransferJob(){
		
	}
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		if(Constant.transferFTPType.equals(type)){
			iTransfer = new FTPTransfer(address, port, user, password, _address, _port, _user, _password);
		}else if(Constant.transferHDFSType.equals(type)){
			iTransfer = new HDFSTransfer(address, port, user, password, _address, _port, _user, _password);
		}else {
			throw new TransferException("not support.type -h for help.");
		}
		iTransfer.transfer(src, dis, delete,option);
	}

}
