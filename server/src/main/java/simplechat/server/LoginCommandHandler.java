package simplechat.server;

import ocsf.server.ConnectionToClient;
import simplechat.commands.command.Command;
import simplechat.commands.command.CommandResult;
import simplechat.commands.parser.StringArgument;

import java.io.IOException;
import java.util.function.Consumer;

public class LoginCommandHandler implements Consumer<CommandResult<ConnectionToClient>>
{

    public static Command<ConnectionToClient> createCommand()
    {
        return Command.<ConnectionToClient>builder()
                .arg("name", StringArgument.singleWord())
                .handle(new LoginCommandHandler());
    }

    @Override
    public void accept(CommandResult<ConnectionToClient> result)
    {
        ConnectionToClient client = result.getContext();
        if (isLoggedIn(client))
        {
            trySendToClient(client, "Server> You're already logged in.");
        }
        else
        {
            client.setInfo("name", result.get("name", String.class));
            trySendToClient(client, "Server> Logged in !");
        }
    }

    private void trySendToClient(ConnectionToClient client, String message)
    {
        try
        {
            client.sendToClient(message);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private boolean isLoggedIn(ConnectionToClient client)
    {
        return client.getInfo("name") != null;
    }


}
