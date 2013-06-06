package org.sbs.transfer.excetption;

public class TransferException extends RuntimeException {

	private static final long serialVersionUID = 59206827751917927L;
	
	public TransferException(String e){
		super(e);
	}
}
