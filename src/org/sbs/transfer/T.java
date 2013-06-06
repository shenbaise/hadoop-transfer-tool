/**
 * 
 */
package org.sbs.transfer;

import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sbs.transfer.core.HdfsReader;
import org.sbs.transfer.core.HdfsWriter;

/**
 * @author zhaoziqiang
 * 测试
 */
public class T {
	// hdfs reader
	public HdfsReader hdfsReader;
	// hdfs writer
	public HdfsWriter hdfsWriter ;
	
	@Before
	public void init(){
		hdfsReader = new HdfsReader("hdfs://172.16.113.10:9000", "opz");
		hdfsWriter = new HdfsWriter("hdfs://172.16.113.10:9000", "opz");
	}
	
	@After
	public void down(){
		
		try {
			hdfsWriter.close();
			hdfsReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void test() throws InterruptedException{
		
		long t = 0;
		try {
			hdfsWriter.open("/aaaaaa.txt");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while(true){
			try {
				hdfsWriter.append("a".getBytes());
				hdfsWriter.sync();// 调用sync方法后，文件内容可以被读到，但文件尺寸读不到。
				t++;
				Thread.sleep(500);
				FileStatus fs =hdfsReader.fileStatus(new Path("/aaaaaa.txt"));
				System.out.println("t:"+t + "\tfs.len:"+fs.getLen());
				
				InputStream in = hdfsReader.inputStream("hdfs://172.16.113.10:9000/aaaaaa.txt", 1000);
				byte[] bs = new byte[100]; 
				try {
					in.read(bs);
					System.out.println(new String(bs));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
//		FileStatus fs =hdfsReader.fileStatus(new Path("/aaaaaa.txt"));
//		System.out.println("\tfs.len:"+fs.getLen());
	}
}
