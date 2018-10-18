package cc.darhao.pipe.thread;

import java.io.IOException;
import java.net.Socket;

public class ReadThread extends Thread{

	private Socket socket;
	
	
	public ReadThread(Socket socket) {
		this.socket = socket;
		setDaemon(true);
	}

	
	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			byte[] bytes = new byte[256];
			try {
				int sum = socket.getInputStream().read(bytes);
				if(sum == -1) {
					break;
				}
				byte[] bytes2 = new byte[sum];
				for (int i = 0; i < bytes2.length; i++) {
					bytes2[i] = bytes[i];
				}
				for (int i = 0; i < MainThread.getWrappers().size(); i++) {
					if(MainThread.getWrappers().get(i).getSocket().equals(socket)) {
						System.out.println("receive " + i +" " + new String(bytes2));
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
