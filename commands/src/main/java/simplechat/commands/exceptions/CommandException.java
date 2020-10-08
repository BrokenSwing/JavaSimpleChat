package simplechat.commands.exceptions;

/**
 * Generic exceptions when handling with command system.
 * @see ArgumentParsingException
 * @see UnknownCommandException
 */
public class CommandException extends Exception
{

    public CommandException(String message)
    {
        super(message);
    }

}
