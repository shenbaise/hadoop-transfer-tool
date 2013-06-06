/**
 * 
 */
package org.sbs.transfer.core;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author zhaoziqiang
 * 
 */
public class Writer {
	private final OutputStream out;

	public Writer(OutputStream outputStream) {
		this.out = outputStream;
	}

	public void write(byte[] byts) throws IOException {
		out.write(byts,0,byts.length);
	}
	
	public void write(byte[] content, int off, int len) throws IOException {
		out.write(content, off, len);
	}
	
	public boolean supportsReopen() {
		return true;
	}

	public void flush() throws IOException {
		// noop
	}
}
