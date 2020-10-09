package simplechat.commands.command;

import simplechat.commands.exceptions.ArgumentParsingException;
import simplechat.commands.exceptions.CommandException;
import simplechat.commands.parser.ArgumentParser;
import simplechat.commands.parser.ParsingResult;
import simplechat.commands.parser.StringWalker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Command<C>
{

    private final Map<String, ArgumentParser<?>> arguments;
    private final Consumer<CommandResult<C>> handler;
    private Command(Map<String, ArgumentParser<?>> arguments, Consumer<CommandResult<C>> handler)
    {
        this.arguments = arguments;
        this.handler = handler;
    }

    public static <C> Builder<C> builder()
    {
        return new Builder<>();
    }

    public void execute(StringWalker walker, C context) throws CommandException
    {
        Map<String, Object> argsValues = new HashMap<>();
        ParsingResult<?> result;
        Iterator<Map.Entry<String, ArgumentParser<?>>> it = arguments.entrySet().iterator();

        while (it.hasNext())
        {
            Map.Entry<String, ArgumentParser<?>> arg = it.next();
            result = arg.getValue().parse(walker);

            if (!result.isOk())
            {
                throw new ArgumentParsingException(arg.getKey(), result.getErrorMessage());
            }

            int spacesCount = walker.skipWhileSpaces();
            if (it.hasNext() && spacesCount == 0)
            {
                throw new CommandException(String.format(
                        "Expected space at column %d.", walker.getCurrentPosition()
                ));
            }

            if (!it.hasNext() && walker.hasNext())
            {
                throw new CommandException(String.format(
                        "Unexpected character at column %d.", walker.getCurrentPosition()
                ));
            }

            argsValues.put(arg.getKey(), result.getValue());
        }

        CommandResult<C> commandResult = CommandResult.create(argsValues, context);
        handler.accept(commandResult);
    }

    public static class Builder<C>
    {

        private final Map<String, ArgumentParser<?>> arguments = new LinkedHashMap<>();

        private Builder()
        {
        }

        public <T> Builder<C> arg(String name, ArgumentParser<T> parser)
        {
            arguments.put(name, parser);
            return this;
        }

        public Command<C> handle(Consumer<CommandResult<C>> handler)
        {
            return new Command<C>(arguments, handler);
        }

    }

}
