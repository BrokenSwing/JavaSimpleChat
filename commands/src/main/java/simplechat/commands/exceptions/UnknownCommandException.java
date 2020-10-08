package simplechat.commands.exceptions;

public class UnknownCommandException extends CommandException
{

    private final String commandName;

    public UnknownCommandException(String errorMessage, String commandName)
    {
        super(errorMessage);
        this.commandName = commandName;
    }

    public String getCommandName()
    {
        return commandName;
    }

}
