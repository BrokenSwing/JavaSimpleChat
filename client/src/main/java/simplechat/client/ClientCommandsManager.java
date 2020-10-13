package simplechat.client;

import simplechat.commands.CommandsManager;
import simplechat.commands.command.CommandResult;
import simplechat.commands.parser.IntArgument;
import simplechat.commands.parser.StringArgument;

public class ClientCommandsManager extends CommandsManager<ChatClient>
{

    public ClientCommandsManager()
    {
        this.newCommand("quit", builder -> builder.handle(this::handleQuitCommand));
        this.newCommand("logoff", builder -> builder.handle(this::handleLogOffCommand));
        this.newCommand("sethost", builder -> builder
                .arg("host", StringArgument.singleWord())
                .handle(this::handleSetHostCommand)
        );
        this.newCommand("setport", builder -> builder
                .arg("port", IntArgument.positive())
                .handle(this::handleSetPortCommand)
        );
        this.newCommand("login", builder -> builder.handle(this::handleLoginCommand));
        this.newCommand("gethost", builder -> builder.handle(this::handleGetHostCommand));
        this.newCommand("getport", builder -> builder.handle(this::handleGetPortCommand));
    }

    private void handleQuitCommand(CommandResult<ChatClient> result)
    {
        result.getContext().quit();
    }

    private void handleLogOffCommand(CommandResult<ChatClient> result)
    {
        result.getContext().logOff();
    }

    private void handleSetHostCommand(CommandResult<ChatClient> result)
    {
        result.getContext().changeHost(result.get("host", String.class));
    }

    private void handleSetPortCommand(CommandResult<ChatClient> result)
    {
        result.getContext().changePort(result.get("port", Integer.class));
    }

    private void handleLoginCommand(CommandResult<ChatClient> result)
    {
        result.getContext().login();
    }

    private void handleGetHostCommand(CommandResult<ChatClient> result)
    {
        result.getContext()
                .getClientUI()
                .infoMessage("Current host is : " + result.getContext().getHost());
    }

    private void handleGetPortCommand(CommandResult<ChatClient> result)
    {
        result.getContext()
                .getClientUI()
                .infoMessage("Current port is : " + result.getContext().getPort());
    }

}
