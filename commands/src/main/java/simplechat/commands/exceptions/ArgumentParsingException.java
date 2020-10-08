package simplechat.commands.exceptions;

/**
 * Exception thrown when an argument is parsed and something unexpected happens (value can't be parsed).
 */
public class ArgumentParsingException extends CommandException
{

    private final String argumentName;
    private final String errorMessage;

    /**
     * Constructs an exceptions that occurs when parsing an argument.
     *
     * @param argumentName The name of the argument being parsed.
     * @param errorMessage The message describing the encountered error
     */
    public ArgumentParsingException(String argumentName, String errorMessage)
    {
        super(String.format("Error parsing argument %s : %s", argumentName, errorMessage));
        this.argumentName = argumentName;
        this.errorMessage = errorMessage;
    }

    /**
     * @return the concerned argument
     */
    public String getArgumentName()
    {
        return argumentName;
    }

    /**
     * @return the error message provided by the parser
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }

}
