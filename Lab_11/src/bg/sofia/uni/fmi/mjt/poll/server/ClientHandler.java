package bg.sofia.uni.fmi.mjt.poll.server;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final PollRepository pollRepository;


    private void validate(Socket clientSocket,PollRepository pollRepository){
        if(clientSocket==null||pollRepository==null){
            throw new IllegalArgumentException("cant be null");
        }
    }
    public ClientHandler(Socket clientSocket, PollRepository pollRepository) {
        validate(clientSocket, pollRepository);
        this.clientSocket = clientSocket;
        this.pollRepository = pollRepository;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String command;
            while ((command = reader.readLine()) != null) {
                String response = processCommand(command);
                writer.println(response);
                if ("disconnect".equalsIgnoreCase(command)) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while communicating with client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing client socket: " + e.getMessage());
            }
        }
    }

    private String handleCreatePoll(String arguments) {
        String[] parts = arguments.split(" ");
        if (parts.length < 3) {
            return "{\"status\":\"ERROR\",\"message\":\"Usage: create-poll <question> <option-1> <option-2> [... <option-N>]\"}";
        }

        String question = parts[0];
        Map<String, Integer> options = new LinkedHashMap<>();
        for(String part:parts){
            options.put(part,0);
        }

        Poll poll = new Poll(question, options);
        int pollId = pollRepository.addPoll(poll);
        return "{\"status\":\"OK\",\"message\":\"Poll " + pollId + " created successfully.\"}";
    }

    private String handleListPolls() {
        Map<Integer, Poll> polls = pollRepository.getAllPolls();
        if (polls.isEmpty()) {
            return "{\"status\":\"ERROR\",\"message\":\"No active polls available.\"}";
        }

        StringBuilder result = new StringBuilder("{\"status\":\"OK\",\"polls\":{");
        for (Map.Entry<Integer, Poll> entry : polls.entrySet()) {
            int id = entry.getKey();
            Poll poll = entry.getValue();

            result.append("\"").append(id).append("\":{\"question\":\"")
                    .append(poll.question()).append("\",\"options\":{");

            poll.options().forEach((option, votes) ->
                    result.append("\"").append(option).append("\":").append(votes).append(","));

            result.setLength(result.length() - 1);
            result.append("}},");
        }

        result.setLength(result.length() - 1);
        result.append("}}");
        return result.toString();
    }

    private String handleSubmitVote(String arguments) {
        String[] parts = arguments.split(" ");
        if (parts.length != 2) {
            return "{\"status\":\"ERROR\",\"message\":\"Usage: submit-vote <poll-id> <option>\"}";
        }

        int pollId;
        try {
            pollId = Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            return "{\"status\":\"ERROR\",\"message\":\"Invalid poll ID.\"}";
        }

        String option = parts[1];
        Poll poll = pollRepository.getPoll(pollId);

        if (poll == null) {
            return "{\"status\":\"ERROR\",\"message\":\"Poll with ID " + pollId + " does not exist.\"}";
        }

        if (!poll.options().containsKey(option)) {
            return "{\"status\":\"ERROR\",\"message\":\"Invalid option. Option " + option + " does not exist.\"}";
        }

        poll.options().put(option, poll.options().get(option) + 1);
        return "{\"status\":\"OK\",\"message\":\"Vote submitted successfully for option: " + option + "\"}";
    }

    private String processCommand(String input) {
        try {
            String[] tokens = input.split(" ", 2);
            Command command = Command.fromString(tokens[0]);
            String arguments = tokens.length > 1 ? tokens[1] : "";

            switch (command) {
                case CREATE_POLL:
                    return handleCreatePoll(arguments);
                case LIST_POLLS:
                    return handleListPolls();
                case SUBMIT_VOTE:
                    return handleSubmitVote(arguments);
                case DISCONNECT:
                    return "{\"status\":\"OK\",\"message\":\"Disconnected successfully.\"}";
                default:
                    return "{\"status\":\"ERROR\",\"message\":\"Unknown command.\"}";
            }
        } catch (IllegalArgumentException e) {
            return "{\"status\":\"ERROR\",\"message\":\"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            return "{\"status\":\"ERROR\",\"message\":\"Unexpected error occurred.\"}";
        }
    }
}
