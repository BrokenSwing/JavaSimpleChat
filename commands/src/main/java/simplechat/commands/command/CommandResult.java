package simplechat.commands.command;

import java.util.Map;

public class CommandResult<C>
{

    private C context;
    private Map<String, Object> argsValues;
    private String errorMessage;
    private CommandResult(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
    private CommandResult(Map<String, Object> argsValues, C ctx)
    {
        this.argsValues = argsValues;
        this.context = ctx;
    }

    static <C> CommandResult<C> errored(String errorMessage)
    {
        return new CommandResult<>(errorMessage);
    }

    static <C> CommandResult<C> ok(Map<String, Object> argsValues, C ctx)
    {
        return new CommandResult<>(argsValues, ctx);
    }

    public <T> T get(String argName, Class<T> argType)
    {
        return argType.cast(argsValues.get(argName));
    }

    public boolean isErrored()
    {
        return this.errorMessage != null;
    }

    public C getContext()
    {
        return context;
    }
}
