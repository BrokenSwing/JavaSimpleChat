package simplechat.client;

import simplechat.commands.CommandsManager;
import simplechat.commands.command.CommandResult;
import simplechat.commands.parser.IntArgument;
import simplechat.commands.parser.StringArgument;
import simplechat.common.SharedConstants;

import java.io.IOException;

public class ClientCommandsManager extends CommandsManager<ChatClient>
{

    public ClientCommandsManager()
    {
        this.newCommand("quit", builder -> builder.handle(this::onQuit));
        this.newCommand("logoff", builder -> builder.handle(this::onLogOff));
        this.newCommand("sethost", builder -> builder
                .arg("host", StringArgument.singleWord())
                .handle(this::onSetHost)
        );
        this.newCommand("setport", builder -> builder
                .arg("port", IntArgument.positive())
                .handle(this::onSetPort)
        );
        this.newCommand("login", builder -> builder.handle(this::onLogin));
        this.newCommand("gethost", builder -> builder.handle(this::onGetHost));
        this.newCommand("getport", builder -> builder.handle(this::onGetPort));
    }

    private void onQuit(CommandResult<ChatClient> result)
    {
        result.getContext().quit();
    }

    private void onLogOff(CommandResult<ChatClient> result)
    {
        ChatClient client = result.getContext();
        if (client.isConnected())
        {
            try
            {
                client.closeConnection();
            }
            catch (IOException e)
            {
                client.getClientUI().errorMessage("Unable to log off.");
            }
        }
        else
        {
            client.getClientUI().errorMessage("You're already logged off.");
        }
    }

    private void onSetHost(CommandResult<ChatClient> result)
    {
        ChatClient client = result.getContext();
        if (client.isConnected())
        {
            client.getClientUI().errorMessage("You can't change the host while you're connected.");
        }
        else
        {
            String newHost = result.get("host", String.class);
            client.setHost(newHost);
            client.getClientUI().infoMessage("Host is now : " + newHost);
        }
    }

    private void onSetPort(CommandResult<ChatClient> result)
    {
        ChatClient client = result.getContext();
        if (client.isConnected())
        {
            client.getClientUI().errorMessage("You can't change the port while you're connected.");
        }
        else
        {
            int newPort = result.get("port", Integer.class);
            client.setPort(newPort);
            client.getClientUI().infoMessage("Port is now : " + newPort);
        }
    }

    private void onLogin(CommandResult<ChatClient> result)
    {
        ChatClient client = result.getContext();
        if (client.isConnected())
        {
            client.getClientUI().errorMessage("You're already connected.");
        }
        else
        {
            try
            {
                client.openConnection();
                client.sendToServer(SharedConstants.COMMAND_PREFIX + "login " + client.getDefaultName());
                client.getClientUI().infoMessage("Connected !");
            }
            catch (IOException e)
            {
                client.getClientUI().errorMessage(String.format(
                        "Unable to connect to %s:%d\n",
                        client.getHost(),
                        client.getPort()
                ));
            }
        }
    }

    private void onGetHost(CommandResult<ChatClient> result)
    {
        result.getContext()
                .getClientUI()
                .infoMessage("Current host is : " + result.getContext().getHost());
    }

    private void onGetPort(CommandResult<ChatClient> result)
    {
        result.getContext()
                .getClientUI()
                .infoMessage("Current port is : " + result.getContext().getPort());
    }

}
