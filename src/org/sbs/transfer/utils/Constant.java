package org.sbs.transfer.utils;

public class Constant {
	
	public static String transferFTPType = "ftp";
	public static String transferHDFSType = "hdfs";
	
	
	// those parms should be read from conf file.
	public static String _address = "172.16.113.10";
	public static String _port = "9000";
	public static String _user = "opz";
	public static String _password = "";
	public static String dis = "/transfer-test";
	
	public static String OVERWRITE = "o";
	public static String IGNORE = "i";
	public static String APPEND = "a";
	
	public enum CopyPolicy {
		
		OverWrite("o"), Ignore("i"), Append("a");
		private final String policy;
		private CopyPolicy(String policy) {
		     this.policy = policy;
		}
		
		public String getPolicy(){
			return this.policy;
		}
	}
}
