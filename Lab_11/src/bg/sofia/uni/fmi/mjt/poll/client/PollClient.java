package bg.sofia.uni.fmi.mjt.poll.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PollClient {

    private final String host;
    private final int port;

    private void validate(String host, int port){

    }

    public PollClient(String host, int port) {
        validate(host,port);
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        PollClient client = new PollClient("localhost", 8080);
        client.start();
    }

    public void start() {
        try (Socket socket = new Socket(host, port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            String command;
            while ((command = console.readLine()) != null) {
                writer.println(command);

                if ("disconnect".equalsIgnoreCase(command)) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error client " + e.getMessage());
        }
    }
}
