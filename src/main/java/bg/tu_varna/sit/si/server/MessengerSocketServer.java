package bg.tu_varna.sit.si.server;
import bg.tu_varna.sit.si.requests.RequestHandler;
import bg.tu_varna.sit.si.requests.SocketRequests;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MessengerSocketServer {

    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true)
            new SocketRequestsHandler(serverSocket.accept()).start();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private static class SocketRequestsHandler extends Thread {
        private Socket clientSocket;
        private ObjectOutputStream objectOutputStream;
        private ObjectInputStream objectInputStream;

        public SocketRequestsHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {

            try {
                objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            SocketRequests.SocketRequestType socketRequestType;
            while (true) {
                try {
                    if ((socketRequestType = (SocketRequests.SocketRequestType) objectInputStream.readObject()) == null)
                        break;

                    RequestHandler handler = new RequestHandler();
                    handler.handleRequest(socketRequestType, objectInputStream, objectOutputStream);
                } catch (IOException | ClassNotFoundException e ) {
                    break;
                }
            }

            try {
                objectInputStream.close();
                objectOutputStream.close();
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
