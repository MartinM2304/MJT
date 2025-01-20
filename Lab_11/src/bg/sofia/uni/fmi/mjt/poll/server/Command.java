package bg.sofia.uni.fmi.mjt.poll.server;

public enum Command {
    CREATE_POLL("create-poll"),
    LIST_POLLS("list-polls"),
    SUBMIT_VOTE("submit-vote"),
    DISCONNECT("disconnect");

    private final String commandName;

    Command(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

    public static Command fromString(String command) {
        for (Command c : Command.values()) {
            if (c.getCommandName().equalsIgnoreCase(command)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid command: " + command);
    }
}
