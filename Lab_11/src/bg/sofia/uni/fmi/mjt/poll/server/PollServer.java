package bg.sofia.uni.fmi.mjt.poll.server;

import bg.sofia.uni.fmi.mjt.poll.server.repository.InMemoryPollRepository;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PollServer {

    private final int port;
    private final PollRepository pollRepository;
    private ServerSocket serverSocket;
    private final ExecutorService executor;

    private void valdiate(int port, PollRepository repository){

    }
    public PollServer(int port, PollRepository pollRepository){
        valdiate(port,pollRepository);
        this.port=port;
        this.pollRepository=pollRepository;
        this.executor= Executors.newCachedThreadPool();
    }

    public static void main(String[] args) {
        PollRepository pollRepository = new InMemoryPollRepository();
        PollServer server = new PollServer(8080, pollRepository);
        server.start();
    }

    public void start(){
        try {
            serverSocket= new ServerSocket(port);

            while (!serverSocket.isClosed()){
                Socket client=serverSocket.accept();
                executor.submit(new ClientHandler(client,pollRepository));
            }
        }catch (IOException e){
            throw new RuntimeException("Error with the server");
        }
    }

    public void stop(){
        try {
            executor.shutdown();
            if(serverSocket !=null && !serverSocket.isClosed()){
                serverSocket.close();
            }
        }catch (IOException e){
            throw new RuntimeException("Error when stopping the server");
        }
    }
}
