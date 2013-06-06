/**
 * 
 */
package org.sbs.transfer.config;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.sbs.transfer.excetption.TransferException;

/**
 * @author zhaoziqiang
 *
 */
public class FTPConfig {
	
	
	public FTPClientConfig fConfig;
	
	public FTPConfig(){}
	
	public FTPConfig(FTPClient ftpClient) {
		try {
			fConfig = new FTPClientConfig(ftpClient.getSystemType(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "zh", null, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param ftpClient
	 */
	public void configureClient(final FTPClient ftpClient){
		if(fConfig == null){
			throw new TransferException("ftp client can not be configured ,fconfig is null.");
		}
		ftpClient.configure(fConfig);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
