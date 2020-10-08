package simplechat.commands;

import simplechat.commands.command.Command;
import simplechat.commands.exceptions.CommandException;
import simplechat.commands.exceptions.UnknownCommandException;
import simplechat.commands.parser.StringArgument;
import simplechat.commands.parser.StringWalker;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that manages all known commands and their execution.
 *
 * @param <C> The context type : the type of an object that will be passed to the execution function of each commands
 */
public class CommandsManager<C>
{

    private final Map<String, Command<C>> commands = new HashMap<>();

    /**
     * Registers a command.
     *
     * @param name The non-empty name of the command
     * @param command The command to register
     */
    public void registerCommand(String name, Command<C> command)
    {
        if (name.isEmpty())
        {
            throw new IllegalArgumentException("The command name must not be empty.");
        }
        commands.put(name.toLowerCase(), command);
    }

    /**
     * Handles the parsing of the command using the given {@link StringWalker}.
     *
     * @param walker The walker to use to parse the command
     * @param context The context object to pass to the command execution handler
     *
     * @throws CommandException If no command with the given name exists.
     */
    public void handleCommandLine(StringWalker walker, C context) throws CommandException
    {
        String commandName = parseCommandName(walker);
        Command<C> command = commands.get(commandName);

        if (command == null)
        {
            throw new UnknownCommandException(
                    String.format("Command with name %s does no exists.", commandName),
                    commandName
            );
        }

        walker.skipWhileSpaces(); /* Clear all spaces between command name and first argument */
        command.execute(walker, context);
    }

    /**
     * Retrieves the name of the command.
     *
     * @param walker The {@link StringWalker} to parse command name from
     * @return the name of the command
     *
     * @throws UnknownCommandException walker is empty or starts with a space character
     */
    private static String parseCommandName(StringWalker walker) throws UnknownCommandException
    {
        if (!walker.hasNext() ||  Character.isSpaceChar(walker.lookAhead()))
        {
            throw new UnknownCommandException(
                    String.format("Expected command name at column %d.", walker.getCurrentPosition()),
                    ""
            );
        }

        /* We can unwrap value blindly because we know next character exists and isn't a space  */
        return StringArgument.singleWord().parse(walker).getValue().toLowerCase();
    }

}
