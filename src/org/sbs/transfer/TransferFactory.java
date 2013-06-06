/**
 * 
 */
package org.sbs.transfer;

import org.sbs.transfer.ftp.FTPTransfer;
import org.sbs.transfer.hdfs.HDFSTransfer;
import org.sbs.transfer.utils.Constant;

/**
 * @author zhaoziqiang
 * transfer factory
 */
public class TransferFactory {
	
	public static ITransfer getTransfer(String type){
		if(type.equals(Constant.transferFTPType))
			return new FTPTransfer();
		else if(type.equals(Constant.transferHDFSType))
			return new HDFSTransfer();
		
		// ...
		else return null;
	}
}
