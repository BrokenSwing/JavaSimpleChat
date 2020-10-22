package simplechat.server;import ocsf.server.AbstractServer;import ocsf.server.ConnectionToClient;import simplechat.commands.CommandsManager;import simplechat.commands.exceptions.CommandException;import simplechat.commands.parser.StringWalker;import simplechat.common.ChatIF;import java.io.IOException;import static simplechat.common.SharedConstants.COMMAND_PREFIX;/** * This class overrides some of the methods in the abstract * superclass in order to give more functionality to the server. * * @author Dr Timothy C. Lethbridge * @author Dr Robert Laganière * @author François Bélanger * @author Paul Holden * @author Florent Hugouvieux * @version October 2020 */public class EchoServer extends AbstractServer{    private final ChatIF serverUI;    private final ServerCommandsManager commandsManager = new ServerCommandsManager();    private final CommandsManager<ConnectionToClient> clientsCommands = new CommandsManager<>();    /**     * Constructs an instance of the echo server.     *     * @param port The port number to connect on.     */    public EchoServer(int port, ChatIF serverUI)    {        super(port);        this.serverUI = serverUI;        this.clientsCommands.registerCommand("login", LoginCommandHandler.createCommand());    }    private static void trySendToClient(ConnectionToClient client, String message)    {        try        {            client.sendToClient(message);        }        catch (IOException e)        {            e.printStackTrace();        }    }    private static boolean isLoggedIn(ConnectionToClient client)    {        return client.getInfo("name") != null;    }    public void handleMessageFromUI(String message)    {        if (message.startsWith(COMMAND_PREFIX))        {            executeServerCommand(message);        }        else        {            String toSend = "Server MSG> " + message;            serverUI.display(toSend);            sendToAllClients(toSend);        }    }    private void executeServerCommand(String message)    {        try        {            StringWalker walker = CommandsManager.walkerFrom(COMMAND_PREFIX, message);            commandsManager.handleCommandLine(walker, this);        }        catch (CommandException commandException)        {            serverUI.errorMessage(commandException.getMessage());        }    }    private void executeClientCommand(ConnectionToClient client, String message)    {        try        {            StringWalker walker = CommandsManager.walkerFrom(COMMAND_PREFIX, message);            clientsCommands.handleCommandLine(walker, client);        }        catch (CommandException commandException)        {            trySendToClient(client, "Server Error> " + commandException.getMessage());        }    }    public ChatIF getServerUI()    {        return serverUI;    }    /**     * This method handles any messages received from the simplechat.client.     *     * @param msg    The message received from the simplechat.client.     * @param client The connection from which the message originated.     */    @Override    public void handleMessageFromClient(Object msg, ConnectionToClient client)    {        if (!(msg instanceof String))        {            return;        }        String message = (String) msg;        if (message.startsWith(COMMAND_PREFIX))        {            executeClientCommand(client, message);        }        else if (isLoggedIn(client))        {            serverUI.display("Message received: " + msg + " from " + client);            this.sendToAllClients(client.getInfo("name") + "> " + message);        }        else        {            trySendToClient(client, "You must log in first.");            try            {                client.close();            }            catch (IOException e)            {                // Ignoring exception            }        }    }    @Override    protected void serverStarted()    {        serverUI.display("Server listening for connections on port " + getPort());    }    @Override    protected void serverStopped()    {        serverUI.display("Server has stopped listening for connections.");    }    @Override    protected synchronized void clientDisconnected(ConnectionToClient client)    {        serverUI.display(client.getName() + " disconnected.");    }    @Override    protected void clientConnected(ConnectionToClient client)    {        serverUI.display(client.getName() + " connected.");    }    @Override    protected synchronized void clientException(ConnectionToClient client, Throwable exception)    {        this.clientDisconnected(client);        Object clientName = client.getInfo("name");        if (clientName != null)        {            sendToAllClients("Server Message> " + clientName + " left !");        }    }    public void quit()    {        try        {            this.close();        }        catch (IOException e)        {            e.printStackTrace();        }        System.exit(0);    }    public void disconnectClients()    {        Thread[] clientThreadList = getClientConnections();        for (Thread thread : clientThreadList)        {            try            {                ((ConnectionToClient) thread).close();            }            catch (Exception ex)            {                // Ignore all exceptions when closing clients.            }        }    }}