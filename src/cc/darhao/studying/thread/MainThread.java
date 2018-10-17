package cc.darhao.studying.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import cc.darhao.studying.socket.SocketWrapper;

public class MainThread {

	private static List<SocketWrapper> wrappers = Collections.synchronizedList(new ArrayList<>());
	
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Scanner scanner = new Scanner(System.in);
		cmdCycle(scanner);
		scanner.close();
	}


	private static void cmdCycle(Scanner scanner) {
		while(true) {
			try {
				System.out.print("Pipe>");
				String cmd = scanner.nextLine();
				String[] cmdAndParas = cmd.split("\\s");
				switch (cmdAndParas[0]) {
				case "list":
					handleListCmd();
					break;
				case "listen":
					handleListenCmd(cmdAndParas);
					break;
				case "connect":
					handleConnectCmd(cmdAndParas);
					break;
				case "cut":
					handleCutCmd(cmdAndParas);
					break;
				case "send":
					handleSendCmd(cmdAndParas);
					break;
				case "help":
					handleHelpCmd();
					break;
				case "quit":
					handleQuitCmd();
					return;
				default:
					System.out.println("\"" + cmdAndParas[0] + "\"" + "command not found");
					break;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("command format error");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	private static void handleQuitCmd() throws IOException {
		for (SocketWrapper socketWrapper : wrappers) {
			socketWrapper.close();
		}
	}


	private static void handleHelpCmd() {
		System.out.println("list");
		System.out.println("    Show all sockets information and state in this program.");
		System.out.println();
		System.out.println("listen port");
		System.out.println("    port: the port is for listen");
		System.out.println("    Create a ServerSocket object and start listening connections.");
		System.out.println();
		System.out.println("connect ip port");
		System.out.println("    ip: remote host ip address");
		System.out.println("    port: remote host port");
		System.out.println("    Create a socket and connect to remote host.");
		System.out.println();
		System.out.println("cut id [ -i | -o ]");
		System.out.println("    id: the id of the disconnected socket");
		System.out.println("    -i: only shutdown iuput");
		System.out.println("    -o: only shutdown output");
		System.out.println("    Close a socket or only shutdown a half of it.");
		System.out.println();
		System.out.println("send id message");
		System.out.println("    id: the socket id to be send");
		System.out.println("    message: the message to be send");
		System.out.println("    Send a message throught specific socket.");
		System.out.println();
		System.out.println("help");
		System.out.println("    Show all tips of this program.");
		System.out.println();
		System.out.println("quit");
		System.out.println("    Quit the program safely and recycle all resources.");
	}


	private static void handleCutCmd(String[] cmdAndParas) throws IOException {
		try{
			SocketWrapper wrapper = wrappers.get(Integer.parseInt(cmdAndParas[1]));
			if(cmdAndParas.length == 2) {
				wrapper.close();
			}else if(cmdAndParas[2].equals("-i")) {
				wrapper.shutdownInput();
			}else if(cmdAndParas[2].equals("-o")){
				wrapper.shutdownOutput();
			}
		}catch (Exception e) {
			System.out.println("\"" + cmdAndParas[1] + "\"" + "id not found");
		}
	}


	private static void handleSendCmd(String[] cmdAndParas) {
		try{
			SocketWrapper wrapper = wrappers.get(Integer.parseInt(cmdAndParas[1]));
			wrapper.send(cmdAndParas[2]);
		}catch (Exception e) {
			System.out.println("\"" + cmdAndParas[1] + "\"" + "id not found");
		}
	}


	private static void handleConnectCmd(String[] cmdAndParas) throws Exception {
		Socket socket = new Socket(cmdAndParas[1], Integer.parseInt(cmdAndParas[2]));
		SocketWrapper wrapper = new SocketWrapper(socket);
		wrappers.add(wrapper);
	}


	private static void handleListCmd() {
		for (int i = 0; i < wrappers.size(); i++) {
			Socket socket = wrappers.get(i).getSocket();
			String src = socket.getLocalAddress().getHostAddress()+":"+socket.getLocalPort();
			String dest = socket.getRemoteSocketAddress().toString().replaceAll("/", "");
			String state = null;
			if(socket.isConnected()) {
				if(socket.isClosed()) {
					state = "Closed";
				}else {
					if(socket.isOutputShutdown()) {
						state = "Closed-Out";
					}else if(socket.isInputShutdown()) {
						state = "Closed-In";
					}else {
						state = "Established";
					}
				}
			}else {
				state = "Connecting";
			}
			System.out.println("["+i+"]"+" "+src+" -> "+dest+" "+state);
		}
	}


	private static void handleListenCmd(String[] cmdAndParas) throws IOException {
		int port = Integer.parseInt(cmdAndParas[1]);
		ServerSocket serverSocket = new ServerSocket(port);
		AcceptThread acceptThread = new AcceptThread(serverSocket);
		acceptThread.start();
	}


	public static List<SocketWrapper> getWrappers() {
		return wrappers;
	}

}
