package simplechat.commands;

import simplechat.commands.command.Command;
import simplechat.commands.exceptions.CommandException;
import simplechat.commands.exceptions.UnknownCommandException;
import simplechat.commands.parser.StringArgument;
import simplechat.commands.parser.StringWalker;

import java.util.HashMap;
import java.util.Map;

public class CommandsManager<C>
{

    private final Map<String, Command<C>> commands = new HashMap<>();

    public void registerCommand(String name, Command<C> command)
    {
        commands.put(name.toLowerCase(), command);
    }

    public void handleCommandLine(StringWalker walker, C context) throws CommandException
    {
        if (!walker.hasNext())
        {
            throw new UnknownCommandException(
                    String.format("Expected command name at column %d.", walker.getCurrentPosition()),
                    ""
            );
        }

        String commandName = StringArgument.singleWord().parse(walker).getValue().toLowerCase();
        Command<C> command = commands.get(commandName);

        if (command == null)
        {
            throw new UnknownCommandException(
                    String.format("Command with name %s does no exists.", commandName),
                    commandName
            );
        }

        walker.skipWhileSpaces();
        command.execute(walker, context);
    }

}
