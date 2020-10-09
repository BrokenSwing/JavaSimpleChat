package simplechat.client;

import simplechat.commands.CommandsManager;
import simplechat.commands.parser.IntArgument;
import simplechat.commands.parser.StringArgument;

public class ClientCommandsManager extends CommandsManager<ChatClient>
{

    public ClientCommandsManager()
    {
        this.newCommand("quit", builder -> builder.handle(r -> r.getContext().quit()));
        this.newCommand("logoff", builder -> builder.handle(r -> r.getContext().logOff()));
        this.newCommand("sethost", builder -> builder
                .arg("host", StringArgument.singleWord())
                .handle(result -> result.getContext().changeHost(result.get("host", String.class)))
        );
        this.newCommand("setport", builder -> builder
                .arg("port", IntArgument.positive())
                .handle(result -> result.getContext().changePort(result.get("port", Integer.class)))
        );
        this.newCommand("login", builder -> builder.handle(r -> r.getContext().login()));
        this.newCommand("gethost", builder -> builder.handle(
                r -> r.getContext().getClientUI().infoMessage("Current host is : " + r.getContext().getHost())
        ));
        this.newCommand("getport", builder -> builder.handle(
                r -> r.getContext().getClientUI().infoMessage("Current port is : " + r.getContext().getPort())
        ));
    }

}
