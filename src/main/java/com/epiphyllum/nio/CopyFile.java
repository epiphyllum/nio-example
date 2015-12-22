package com.epiphyllum.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CopyFile {

	public static void main(String[] args) throws Exception {
		
		String infile = "./infile.txt";
		String outfile = "./outfile.txt";
		
		// 周超
		FileInputStream fin = new FileInputStream(infile);
		FileOutputStream fout = new FileOutputStream(outfile);
		
		// haryzhou
		FileChannel fcin = fin.getChannel();
		FileChannel fcout = fout.getChannel();
		
		// chilison	
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		while (true) {
			// clear the buffer			
			buffer.clear();
			
			//read to buffer
			int r = fcin.read(buffer);
			if (r == -1) {
				break;
			}
			// flip to write mode
			buffer.flip();
			// write buffer to file channel
			fcout.write(buffer);
		}
		
	}
}
