package cc.darhao.pipe.socket;

import java.io.IOException;
import java.net.Socket;

import cc.darhao.pipe.thread.ReadThread;
import cc.darhao.pipe.thread.WriteThread;

public class SocketWrapper {
	
	private WriteThread writeThread;
	
	private ReadThread readThread;
	
	private Socket socket;

	
	public SocketWrapper(Socket socket) {
		this.writeThread = new WriteThread(socket);
		writeThread.start();
		this.readThread = new ReadThread(socket);
		readThread.start();
		this.socket = socket;
	}


	public Socket getSocket() {
		return socket;
	}


	public void send(String string) {
		writeThread.getSendQ().offer(string);
	}
	
	
	public void close() throws IOException {
		readThread.interrupt();
		writeThread.interrupt();
		socket.close();
	}
	
	
	public void shutdownInput() throws IOException {
		readThread.interrupt();
		socket.shutdownInput();
	}
	
	
	public void shutdownOutput() throws IOException {
		writeThread.interrupt();
		socket.shutdownOutput();
	}
}
