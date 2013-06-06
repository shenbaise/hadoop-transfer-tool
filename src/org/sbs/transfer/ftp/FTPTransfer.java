/**
 * 
 */
package org.sbs.transfer.ftp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.helpers.DateTimeDateFormat;
import org.sbs.transfer.ITransfer;
import org.sbs.transfer.core.FileUtils;
import org.sbs.transfer.core.HdfsWriter;
import org.sbs.transfer.transaction.log.TransLog;
import org.sbs.transfer.transaction.log.TransLog.Tlog;
import org.sbs.transfer.utils.Constant;
import org.sbs.transfer.utils.Constant.CopyPolicy;
import org.sbs.transfer.utils.DateTimeUtil;

import com.google.inject.Inject;

/**
 * @author zhaoziqiang
 * 负责将Ftp文件传输到平台hdfs上。
 */
public class FTPTransfer implements Serializable,ITransfer{

	private static final long serialVersionUID = 334259245026081721L;
	
	// ftp 
	public String address = "";
	public String port = "";
	public String user = "";
	public String password = "";
	public String src = "";
	// hdfs
	public String _address = Constant._address;
	public String _port = Constant._port;
	public String _user = Constant._user;
	public String _password = Constant._password;
	
	public String dis = "";
	// ftpclient
	public FTPClient ftpClient ;
	public FTPClientConfig ftpClientConfig ;
	// hdfs writer
	public HdfsWriter hdfsWriter ;
	
	StringBuilder cpDir = new StringBuilder();
	public String controlEncoding = "GBK";
	
	List<String> list = new ArrayList<String>();
	
	private TransLog transLog;
	
	public FTPTransfer(){};
	
	@Inject
	public FTPTransfer(String address, String port, String user,
			String password, String _address, String _port,
			String _user, String _password) {
		super();
		this.address = address;
		this.port = port;
		this.user = user;
		this.password = password;
		this._address = _address;
		this._port = _port;
		this._user = _user;
		this._password = _password;
		try {
			// setup ftp & connect
			ftpClient = new FTPClient();
			ftpClient.setControlEncoding("gbk");
//			ftpClient.connect(address, Integer.parseInt(port));
			ftpClient.connect(address);
			ftpClient.login(user, password);
			// config ftp 
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			ftpClientConfig = new FTPClientConfig(ftpClient.getSystemType(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "zh", null, null);
			ftpClient.configure(ftpClientConfig);
			// new a hdfs writer
			hdfsWriter = new HdfsWriter("hdfs://"+this._address+":"+this._port, this._user);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
			log.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e);
		}
		// new a translog object
		try {
			transLog = new TransLog(new FileOutputStream(new File(DateTimeDateFormat.getDateInstance().format(new Date()))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.warn("create translog file failed!");
		}
	}
	
	/**
	 * Transfer files
	 * @param src
	 * @param dis
	 */
	@Override
	public void transfer(String src,String dis,boolean delete,CopyPolicy option){
		this.src = src;
		this.dis = dis;
		FileUtils fileUtils = new FileUtils(hdfsWriter);
		try {
			List<String> files = getDirs(src);
			for(String file:files){
				cpDir.setLength(0);
				cpDir.append(dis).append(file.substring(file.indexOf(src)+src.length()));
				System.out.println(cpDir.toString());
				// new a log
				FTPFile ftpFile = ftpClient.mlistFile(file);
//				currentLog = new Tlog(DateTimeUtil.getDateTime(), this.address, this.port, file, String.valueOf(ftpFile.getSize()));
				fileUtils.copy(ftpClient.retrieveFileStream(file),cpDir.toString() );
				// after call retrevefilestream ,call ftpClient.completePendingCommand();
				
				ftpClient.completePendingCommand();
			}
			if(delete){
				ftpClient.dele(src);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			
		}

		String tem = cpDir.toString();
		cpDir.delete(0, cpDir.length());
		if(tem.length()>0)
			cpDir.append(tem.subSequence(0, tem.lastIndexOf("/")));
		// cd ..
		try {
			ftpClient.cdup();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * get files under dir.
	 * @param dir
	 * @return
	 */
	public List<String> getDirs(String dir) throws Exception{
		
		try {
			// cd dir
			if(ftpClient.changeWorkingDirectory(dir)){
				FTPFile[] ffs = ftpClient.listFiles();
				if(ffs!=null){
					for(int i = 0;i<ffs.length;i++){
//						System.out.println(ftpClient.printWorkingDirectory()+"/"+ffs[i].getName()+"......");
						if(ffs[i].getType()==1){
							getDirs(ffs[i].getName());
						}else {
//							System.out.println(ftpClient.printWorkingDirectory()+"/"+ffs[i].getName()+"......");
							list.add(ftpClient.printWorkingDirectory()+"/"+ffs[i].getName());
						}
					}
				}
				// cd ..
				ftpClient.cdup();
			}else if(ftpClient.getModificationTime(dir) != null){
				list.add(dir);
			}
			else {
				throw new Exception("on such dir or file on ftp.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public String getControlEncoding() {
		return controlEncoding;
	}

	public void setControlEncoding(String controlEncoding) {
		this.controlEncoding = controlEncoding;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// test
		FTPTransfer transfer = new  FTPTransfer("192.168.70.100", null, "anonymous", "anonymous", "172.16.113.10", "9000", "opz", "");
//		transfer.transfer("zzq/downloads", "/ftp-dis");
//		transfer.transfer("zzq/1.jpg", "/ftp-dis");
//		transfer.transfer("zzq", "/ftp-dis");
		List<String> list;
		try {
			list = transfer.getDirs("");
			for(String ff:list){
				System.out.println(ff);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
