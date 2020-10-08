package simplechat.commands.exceptions;

/**
 * Exception thrown by the {@link simplechat.commands.CommandsManager} when it must handle a command line
 * to correspond to no command.
 */
public class UnknownCommandException extends CommandException
{

    private final String commandName;

    /**
     * Constructs an exception that occurs when trying to handle a command to do not exist.
     * @param errorMessage The message of the exception
     * @param commandName The name of the command that do not exist
     */
    public UnknownCommandException(String errorMessage, String commandName)
    {
        super(errorMessage);
        this.commandName = commandName;
    }

    /**
     * @return the name of the concerned command
     */
    public String getCommandName()
    {
        return commandName;
    }

}
