/**
 * 
 */
package org.sbs.transfer.hdfs;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.hadoop.fs.Path;
import org.sbs.transfer.ITransfer;
import org.sbs.transfer.core.FileUtils;
import org.sbs.transfer.core.HdfsReader;
import org.sbs.transfer.core.HdfsWriter;
import org.sbs.transfer.utils.Constant.CopyPolicy;

/**
 * @author zhaoziqiang
 * 将其他hdfs上的文件传输到平台hdfs（也可以是其他hdfs）。
 */
public class HDFSTransfer implements Serializable,ITransfer{
	private static final long serialVersionUID = -907463183710514442L;
	
	// src hdfs
	public String address = "";
	public String port = "";
	public String user = "";
	public String password = "";
	public String src = "";
	// dis hdfs
	public String _address = "";
	public String _port = "";
	public String _user = "";
	public String _password = "";
	public String dis = "";
	// hdfs reader
	public HdfsReader hdfsReader;
	// hdfs writer
	public HdfsWriter hdfsWriter ;
	
	StringBuilder cpDir = new StringBuilder();
	
	public HDFSTransfer(){}

	public HDFSTransfer(String address, String port, String user,
			String password, String _address, String _port, String _user,
			String _password) {
		super();
		this.address = address;
		this.port = port;
		this.user = user;
		this.password = password;
		this._address = _address;
		this._port = _port;
		this._user = _user;
		this._password = _password;
		
		hdfsReader = new HdfsReader("hdfs://"+this.address+":"+this.port, this.user);
		hdfsWriter = new HdfsWriter("hdfs://"+this._address+":"+this._port, this._user);
	};
	
	/**
	 * 将src下的文件传输到dir
	 * @param src
	 * @param dis
	 */
	@Override
	public void transfer(String src,String dis,boolean delete,CopyPolicy option){
		FileUtils fileUtils = new FileUtils(hdfsWriter);
		List<Path> files = this.hdfsReader.getDirs(new Path(src));
		for(Path Path:files){
			cpDir.delete(0, cpDir.length());
			cpDir.append(Path.toString());
			
			System.out.println(Path.toString());
			// copy
			fileUtils.copy(hdfsReader.inputStream(Path.toString(),10240), dis+cpDir.substring(cpDir.indexOf(src)+src.length()));
		}
		if(delete){
			try {
				hdfsReader.delete(src);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		HDFSTransfer hdfsTransfer = new HDFSTransfer("172.16.113.10", "9000", "opz","", "172.16.113.10", "9000", "opz","");
		hdfsTransfer.transfer("/ftp-dis", "/_ftp-dis",false,CopyPolicy.OverWrite);
	}
	
}
