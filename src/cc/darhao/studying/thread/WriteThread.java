package cc.darhao.studying.thread;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WriteThread extends Thread{

	private Socket socket;
	
	private BlockingQueue<String> sendQ = new LinkedBlockingQueue<>();
	
	
	public WriteThread(Socket socket) {
		this.socket = socket;
	}


	public BlockingQueue<String> getSendQ() {
		return sendQ;
	}

	
	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			String string = sendQ.poll();
			if(string != null) {
				try {
					socket.getOutputStream().write(string.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
