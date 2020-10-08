package simplechat.commands.exceptions;

public class ArgumentParsingException extends CommandException
{

    private final String argumentName;
    private final String errorMessage;

    public ArgumentParsingException(String argumentName, String errorMessage)
    {
        super(String.format("Error parsing argument %s : %s", argumentName, errorMessage));
        this.argumentName = argumentName;
        this.errorMessage = errorMessage;
    }

    public String getArgumentName()
    {
        return argumentName;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

}
