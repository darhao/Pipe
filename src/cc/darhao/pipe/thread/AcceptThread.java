package cc.darhao.pipe.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cc.darhao.pipe.socket.SocketWrapper;

public class AcceptThread extends Thread{

	private ServerSocket serverSocket;
	
	
	public AcceptThread(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		setDaemon(true);
	}
	
	
	@Override
	public void run() {
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				SocketWrapper wrapper = new SocketWrapper(socket);
				MainThread.getWrappers().add(wrapper);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
