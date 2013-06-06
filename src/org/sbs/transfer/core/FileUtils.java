/**
 * 
 */
package org.sbs.transfer.core;

import java.io.IOException;
import java.io.InputStream;

import org.sbs.transfer.utils.Constant.CopyPolicy;

/**
 * @author zhaoziqiang
 * 负责文件拷贝
 */
public class FileUtils {
	
	private final HdfsWriter hdfsWriter;
	
	// buff
	public static int buffSize = 102400;	// 100k
	
	byte[] buff = new byte[buffSize];
	// flush Size
	private static int flushSzie = 64;		//
	private int counter;
	private int fileFlag;
	/**
	 * construction
	 * @param hdfsWriter
	 */
	public FileUtils(HdfsWriter hdfsWriter){
		this.hdfsWriter = hdfsWriter;
	}
	/**
	 * copy a file
	 * @param s
	 * @param d
	 */
	public void copy(InputStream in,String d){
		// new a file
		try {
			hdfsWriter.open(d);
			// transfer
			while((fileFlag = in.read(buff))>0){
				++counter;
				hdfsWriter.append(buff,0,fileFlag);
				// flush it .
				if(counter%flushSzie==0)
					hdfsWriter.sync();
			}
			// close hdfs file .
			hdfsWriter.close();
			// close stream
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		System.out.println(CopyPolicy.Append.getPolicy());
	}
}
