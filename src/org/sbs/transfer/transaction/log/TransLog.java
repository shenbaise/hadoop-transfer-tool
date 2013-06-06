/**
 * 
 */
package org.sbs.transfer.transaction.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author zhaoziqiang
 * 数据提取日志记录格式
 */
public class TransLog {
	
	private List<Tlog> tlogs = Lists.newArrayList();
	// log file outputstream
	private OutputStream out;
	
	public TransLog(OutputStream logFileOutputStream){
		this.out = logFileOutputStream;
	}
	
	public void put(Tlog log){
		tlogs.add(log);
	}
	
	public void put(List<Tlog>  logs){
		tlogs.addAll(logs);
	}
	
	/**
	 * put a message and log it.
	 * @param log
	 * @throws IOException
	 */
	public void put$write(Tlog log) throws IOException{
		put(log);
		write();
	}
	
	/**
	 * write logs
	 * @param out
	 * @throws IOException
	 */
	public void write() throws IOException{
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for(Tlog log:tlogs){
			sb.append(log.toString());
			if(count%100==0){
				out.write(sb.toString().getBytes());
				out.flush();
				sb.delete(0, sb.length());
			}
			count++;
		}
		if(sb.length()>0){
			out.write(sb.toString().getBytes());
			out.flush();
			sb.delete(0, sb.length());
		}
		// clear tlogs
		tlogs.clear();
	}
	
	/**
	 * destroy Translog
	 */
	public void destroy(){
		try {
			tlogs.clear();
			tlogs = null;
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// test
	public void test(){
		Tlog l = new Tlog("2010-01-01 1:1:1", "192.168.1.1", "22", "abaceef.tdx", "100",  "99", "false");
		System.out.print(l.toString());
		try {
			put$write(l);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TransLog transLog;
		try {
			transLog = new TransLog(new FileOutputStream(new File("Tlog"), true));
			transLog.test();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @author zhaoziqiang
	 * Log class
	 */
	public class Tlog{
		
		public String time;
		public String ip ;
		public String port;
		public String srcPath;
		public String size;
		public String TSize;
		public String flag;
		
		public Tlog(){}
		
		public Tlog(String time, String ip, String port, String srcPath,
				String size, String tSize, String flag) {
			super();
			this.time = time;
			this.ip = ip;
			this.port = port;
			this.srcPath = srcPath;
			this.size = size;
			this.TSize = tSize;
			this.flag = flag;
		}
		
		public Tlog(String time,String ip,String port,String srcPath,String size){
			this.time = time;
			this.ip = ip;
			this.port = port;
			this.srcPath = srcPath;
			this.size = size;
		}
		
		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public String getPort() {
			return port;
		}

		public void setPort(String port) {
			this.port = port;
		}

		public String getSrcPath() {
			return srcPath;
		}

		public void setSrcPath(String srcPath) {
			this.srcPath = srcPath;
		}

		public String getSize() {
			return size;
		}

		public void setSize(String size) {
			this.size = size;
		}

		public String getTSize() {
			return TSize;
		}

		public void setTSize(String tSize) {
			TSize = tSize;
		}

		public String getFlag() {
			return flag;
		}

		public void setFlag(String flag) {
			this.flag = flag;
		}

		/**
		 * get a log string
		 */
		public String toString(){
			StringBuilder sb = new StringBuilder();
			return sb.append(this.time).append("\t")
					.append(this.ip).append("\t")
					.append(this.port).append("\t")
					.append(this.srcPath).append("\t")
					.append(this.size).append("\t")
					.append(this.TSize).append("\t")
					.append(this.flag).append("\n").toString();
		}
	}
}
