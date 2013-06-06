/**
 * 
 */
package org.sbs.transfer.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * @author zhaoziqiang
 *	从Hdfs上读取数据流
 */
public class HdfsReader {
	
	private Configuration conf;
	private FileSystem hdfs;
	private List<Path> list;
	/**
	 * construction
	 * @param url
	 * @param user
	 */
	public HdfsReader(String url,String user){
		conf = new Configuration();
		list = new ArrayList<Path>();
		try {
			hdfs = FileSystem.get(URI.create(url),conf,user);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * construct
	 */
	public HdfsReader(){
		conf = new Configuration();
		list = new ArrayList<Path>();
	}
	/**
	 * get inputstream.
	 * @param filePath
	 * @param bufferSize
	 * @return
	 */
	public InputStream inputStream(String filePath, int bufferSize) {
		Path path = new Path(filePath);
		try {
			return hdfs.open(path,bufferSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * get all files and dirs under given path.
	 * @param path
	 * @return
	 */
	public List<Path> getDirs(Path path){
		try {
			FileStatus[] fss = hdfs.listStatus(path);
			if(fss!=null){
				for(int i = 0;i<fss.length;i++){
					if(fss[i].isDir()){
						getDirs(fss[i].getPath());
					}else {
						list.add(fss[i].getPath());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * get file status
	 * @param path
	 * @return
	 */
	public FileStatus fileStatus(Path path){
		try {
			return hdfs.getFileStatus(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * get working directory.
	 * @return
	 */
	public Path getWorkingDir(){
		return hdfs.getWorkingDirectory();
	}
	/**
	 * delete
	 * @param src - filePath or directory
	 * @return
	 * @throws IOException
	 */
	public boolean delete(String src) throws IOException{
		return hdfs.delete(new Path(src),true);
	}
	/**
	 * get replicas num.
	 * @param path
	 * @return
	 */
	public int getReplicas(Path path){
		try {
			return hdfs.getFileStatus(path).getReplication();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}
	
	/**
	 * @param path
	 * @return
	 */
	public boolean exist(Path path){
		try {
			return hdfs.exists(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void close() throws IOException{
		hdfs.close();
	}
	
	public static void main(String[] args)  {
		System.out.println(System.currentTimeMillis());
		HdfsReader hdfsReader  = new HdfsReader("hdfs://172.16.113.10:9000","opz");
		
		InputStream in = hdfsReader.inputStream("hdfs://172.16.113.10:9000/hello.txt", 1000);
		byte[] bs = new byte[100]; 
		try {
			in.read(bs);
			System.out.println(new String(bs));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis());
	}
}