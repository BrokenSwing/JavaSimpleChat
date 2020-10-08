package simplechat.commands.command;

import java.util.Map;

/**
 * A class that represents the result of a command parsing when everything gone without error.
 *
 * @param <C> The type of the context object
 */
public class CommandResult<C>
{

    private final C context;
    private final Map<String, Object> argsValues;

    private CommandResult(Map<String, Object> argsValues, C ctx)
    {
        this.argsValues = argsValues;
        this.context = ctx;
    }

    /**
     * Constructs a CommandResult with the values of the parsed arguments.
     *
     * @param argsValues A map of arguments to their parsed values
     * @param ctx The context object
     * @param <C> The type of the context object
     *
     * @return a command result
     */
    static <C> CommandResult<C> create(Map<String, Object> argsValues, C ctx)
    {
        return new CommandResult<>(argsValues, ctx);
    }

    /**
     * Retrieves the parsed value for an argument.
     *
     * @param argName The name of the argument
     * @param argType The class of the type of the argument (to be casted to)
     * @param <T> The type of the argument
     *
     * @return the parsed value of the argument
     */
    public <T> T get(String argName, Class<T> argType)
    {
        return argType.cast(argsValues.get(argName));
    }

    /**
     * @return the context object passed to the command manager when asked to handle the command
     */
    public C getContext()
    {
        return context;
    }
}
