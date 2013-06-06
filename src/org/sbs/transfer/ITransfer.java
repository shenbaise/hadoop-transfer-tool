/**
 * 
 */
package org.sbs.transfer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sbs.transfer.utils.Constant.CopyPolicy;

/**
 * @author zhaoziqiang
 * Transfer
 */
public interface ITransfer {
	public Log log = LogFactory.getLog(ITransfer.class);
	public void transfer(String src,String dis,boolean delete,CopyPolicy option);
//	public void transLog();
}
