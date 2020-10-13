package simplechat.server;

import simplechat.commands.CommandsManager;
import simplechat.commands.command.CommandResult;
import simplechat.commands.parser.IntArgument;

import java.io.IOException;

public class ServerCommandsManager extends CommandsManager<EchoServer>
{

    public ServerCommandsManager()
    {
        newCommand("quit", b -> b.handle(this::onQuit));
        newCommand("stop", b -> b.handle(this::onStop));
        newCommand("close", b -> b.handle(this::onClose));
        newCommand("setport", b -> b.arg("port", IntArgument.positive()).handle(this::onSetPort));
        newCommand("start", b -> b.handle(this::onStart));
        newCommand("getport", b-> b.handle(this::onGetPort));
    }

    private void onQuit(CommandResult<EchoServer> result)
    {
        result.getContext().quit();
    }

    private void onStop(CommandResult<EchoServer> result)
    {
        if (result.getContext().isListening())
        {
            result.getContext().stopListening();
            result.getContext().getServerUI().infoMessage("Server stopped listening.");
        }
        else
        {
            result.getContext().getServerUI().errorMessage("Server is not listening.");
        }
    }

    private void onClose(CommandResult<EchoServer> result)
    {
        if (result.getContext().getClientConnections().length > 0)
        {
            result.getContext().stopListening();
            result.getContext().disconnectClients();
            result.getContext().getServerUI().infoMessage("Server closed.");
        }
        else
        {
            result.getContext().getServerUI().errorMessage("Server already closed.");
        }
    }

    private void onSetPort(CommandResult<EchoServer> result)
    {
        if (result.getContext().isListening())
        {
            result.getContext().getServerUI().errorMessage("Unable to change port. Server is listening.");
        }
        else
        {
            int port = result.get("port", Integer.class);
            result.getContext().setPort(port);
            result.getContext().getServerUI().infoMessage("Server port changed to " + port);
        }
    }

    private void onStart(CommandResult<EchoServer> result)
    {
        if (result.getContext().isListening())
        {
            result.getContext().getServerUI().infoMessage("Server already listening.");
        }
        else
        {
            try
            {
                result.getContext().listen();
                result.getContext().getServerUI().infoMessage("Server is now listening.");
            }
            catch (IOException e)
            {
                result.getContext().getServerUI().errorMessage("Unable to start server.");
                e.printStackTrace();
            }
        }
    }

    private void onGetPort(CommandResult<EchoServer> result)
    {
        int port = result.getContext().getPort();
        result.getContext().getServerUI().infoMessage("Server port is : " + port);
    }

}
