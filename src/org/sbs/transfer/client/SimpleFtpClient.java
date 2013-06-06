/**
 * 
 */
package org.sbs.transfer.client;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.sbs.transfer.config.ServerInfo;


/**
 * @author zhaoziqiang
 *
 */
public class SimpleFtpClient implements SimpleClient{
	
	public ServerInfo serverInfo;
	
	public FTPClient ftpClient; 
	
	public SimpleFtpClient(){
		
	}
	
	public SimpleFtpClient(ServerInfo serverInfo){
		this.serverInfo = serverInfo;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleFtpClient simpleFtpClient = new SimpleFtpClient();
		simpleFtpClient.getFtpClient(new ServerInfo("192.168.70.100", null, "anonymous", "anonymous"));
		
		try {
			simpleFtpClient.getDirs("/");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String clientType() {
		return null;
	}


	
	public FTPClient getFtpClient(ServerInfo serverInfo){
		
		// setup ftp & connect
		ftpClient = new FTPClient();
		ftpClient.setControlEncoding("gbk");
//					ftpClient.connect(address, Integer.parseInt(port));
		
					
		try {
			ftpClient.connect(serverInfo.getAddress());
			ftpClient.login(serverInfo.getUser(), serverInfo.getPassword());
			// config ftp 
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			FTPClientConfig ftpClientConfig = new FTPClientConfig(ftpClient.getSystemType(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "zh", null, null);
			ftpClient.configure(ftpClientConfig);
			// config ftp 
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ftpClient;
	}
	
	List<String> list = new ArrayList<String>();
	public List<String> getDirs(String dir) throws Exception{
		
		try {
			// cd dir
			if(ftpClient.changeWorkingDirectory(dir)){
				FTPFile[] ffs = ftpClient.listFiles();
				if(ffs!=null){
					for(int i = 0;i<ffs.length;i++){
//						System.out.println(ftpClient.printWorkingDirectory()+"/"+ffs[i].getName()+"......");
//						FileUtils.writeStringToFile(new File("d:\\ftp.txt"), ftpClient.printWorkingDirectory()+"/"+ffs[i].getName()+"\n", true);
						
						if(ffs[i].getType()==1){
							getDirs(ffs[i].getName());
						}else {
//							System.out.println(ftpClient.printWorkingDirectory()+"/"+ffs[i].getName()+"......");
							FileUtils.writeStringToFile(new File("d:\\ftp0.txt"), ftpClient.printWorkingDirectory()+"/"+ffs[i].getName()+"\n", true);
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
}
