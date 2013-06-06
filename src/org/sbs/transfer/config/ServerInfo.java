/**
 * 
 */
package org.sbs.transfer.config;

/**
 * @author zhaoziqiang
 *
 */
public class ServerInfo {
	// ftp 
	public String address = "";
	public String port = "";
	public String user = "";
	public String password = "";
	
	public ServerInfo(){
		
	}
	
	public ServerInfo(String address, String port, String user, String password) {
		super();
		this.address = address;
		this.port = port;
		this.user = user;
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
