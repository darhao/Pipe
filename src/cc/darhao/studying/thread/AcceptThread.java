package cc.darhao.studying.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cc.darhao.studying.socket.SocketWrapper;

public class AcceptThread extends Thread{

	private ServerSocket serverSocket;
	
	
	public AcceptThread(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
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
