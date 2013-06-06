/**
 * 
 */
package org.sbs.transfer.core;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.sbs.transfer.utils.Constant.CopyPolicy;

/**
 * @author zhaoziqiang
 * 
 */
public class HdfsWriter {

	private FSDataOutputStream outStream;

	private Writer writer;
	
	private Configuration conf;
	
	private FileSystem hdfs;
	
	
	/**
	 * construction
	 * @param url
	 * @param user
	 */
	public HdfsWriter(String url,String user){
		conf = new Configuration();
		try {
			hdfs = FileSystem.get(URI.create(url),conf,user);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * open a file.if exist,append it.
	 * your hadoop should support append.
	 * @param filePath
	 * @throws IOException
	 */
	public void open(String filePath) throws IOException {
			
		Path dstPath = new Path(filePath);
		boolean appending = false;
		if (conf.getBoolean("hdfs.append.support", false) == true && 
				hdfs.isFile(dstPath)) {
			outStream = hdfs.append(dstPath);
			appending = true;
		} else {
			outStream = hdfs.create(dstPath);
		}
		
		writer = new Writer(outStream);
		if (appending && !writer.supportsReopen()) {
			outStream.close();
			writer = null;
			throw new IOException("your hdfs does not support append");
		}
	}
	
	/**
	 * 
	 * @param filePath
	 * @param option
	 * @return
	 * @throws IOException
	 */
	public boolean open(String filePath,CopyPolicy option) throws IOException {
		
		Path dstPath = new Path(filePath);
		boolean appending = false;
		if (conf.getBoolean("hdfs.append.support", false) == true && 
				hdfs.isFile(dstPath)) {
			outStream = hdfs.append(dstPath);
			appending = true;
		} else {
			outStream = hdfs.create(dstPath);
		}
		
		writer = new Writer(outStream);
		if (appending && !writer.supportsReopen()) {
			outStream.close();
			writer = null;
			throw new IOException("your hdfs does not support append");
		}
		return false;
	}
	
	/**
	 * open a file . if file exist，you can overwirte it.
	 * @param filePath
	 * @param overwirte
	 * @throws IOException 
	 */
	public void open(String filePath,boolean overwirte) throws IOException{
		Path dstPath = new Path(filePath);
		boolean appending = false;
		if (conf.getBoolean("hdfs.append.support", false) == true
				&& hdfs.isFile(dstPath)) {
			outStream = hdfs.append(dstPath);
			appending = true;
		} else {
			outStream = hdfs.create(dstPath,overwirte);
		}
		
		writer = new Writer(outStream);
		if (appending && !writer.supportsReopen()) {
			outStream.close();
			writer = null;
			throw new IOException("your hdfs does not support append");
		}
	}
	
	/**
	 * mkdir
	 * @param dir
	 */
	public void mkdir(String dir){
		Path dirPath = new Path(dir);
		try {
			if(!hdfs.exists(dirPath))
				hdfs.mkdirs(dirPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * append byte content
	 * @param content
	 * @throws IOException
	 */
	public void append(byte[] content) throws IOException {
		writer.write(content);
	}
	public void append(byte[] content,int off,int len) throws IOException {
		writer.write(content,off,len);
	}
	
	/**
	 * sync
	 * @throws IOException
	 */
	public void sync() throws IOException {
		writer.flush();
		outStream.flush();
		outStream.sync();
	}
	
	/**
	 * close stream.
	 * @throws IOException
	 */
	public void close() throws IOException {
		writer.flush();
		outStream.flush();
		outStream.sync();
		outStream.close();
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HdfsWriter hdfsWriter = new HdfsWriter("hdfs://172.16.113.10:9000","opz");
		
		try {
			// write file
			// open it 
			hdfsWriter.open("/abc");
			// append
			hdfsWriter.append("hello you !".getBytes());
			// sync it 
			hdfsWriter.sync();
			// test end
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
//