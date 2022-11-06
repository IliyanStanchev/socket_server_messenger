package bg.tu_varna.sit.si.server;
import bg.tu_varna.sit.si.models.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MessengerSocketServer {

    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true)
            new EchoClientHandler(serverSocket.accept()).start();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private ObjectOutputStream out;
        private ObjectInputStream in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {

            try {
                out = new ObjectOutputStream(clientSocket.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                in = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            User inputLine;
            while (true) {
                try {
                    if (!((inputLine = (User) in.readObject()) != null))
                        break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                if (".".equals(inputLine)) {
                    try {
                        out.writeObject("bye");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                try {
                    out.writeObject(inputLine);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
