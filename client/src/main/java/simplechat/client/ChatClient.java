// This file contains material supporting section 3.7 of the textbook:// "Object Oriented Software Engineering" and is issued under the open-source// license found at www.lloseng.com package simplechat.client;import ocsf.client.AbstractClient;import simplechat.commands.CommandsManager;import simplechat.commands.exceptions.CommandException;import simplechat.commands.exceptions.UnknownCommandException;import simplechat.commands.parser.StringWalker;import simplechat.common.ChatIF;import simplechat.common.SharedConstants;import java.io.IOException;/** * This class overrides some of the methods defined in the abstract * superclass in order to give more functionality to the simplechat.client. * * @author Dr Timothy C. Lethbridge * @author Dr Robert Laganiè; * @author François Bélanger * @author Florent Hugouvieux * @version October 2020 */public class ChatClient extends AbstractClient{    /**     * The interface type variable.  It allows the implementation of     * the display method in the simplechat.client.     */    private final ChatIF clientUI;    private final ClientCommandsManager commandsManager = new ClientCommandsManager();    /**     * Constructs an instance of the chat simplechat.client.     *     * @param host     The server to connect to.     * @param port     The port number to connect on.     * @param clientUI The interface type variable.     */    public ChatClient(String host, int port, ChatIF clientUI)            throws IOException    {        super(host, port);        this.clientUI = clientUI;        openConnection();    }    /**     * This method handles all data that comes in from the server.     *     * @param msg The message from the server.     */    public void handleMessageFromServer(Object msg)    {        clientUI.display(msg.toString());    }    /**     * This method handles all data coming from the UI     *     * @param message The message from the UI.     */    public void handleMessageFromClientUI(String message)    {        if (message.startsWith(SharedConstants.COMMAND_PREFIX))        {            executeCommand(message);        }        else        {            trySendToServer(message);        }    }    private void trySendToServer(String message)    {        if (isConnected())        {            try            {                sendToServer(message);            }            catch (IOException e)            {                clientUI.errorMessage("We were unable to send this message to the server.");            }        }        else        {            clientUI.errorMessage("You must connect first. " +                    "Use #connect to connect or, #setPort <port> and #setHost <host> to change connection settings.");        }    }    private void executeCommand(String message)    {        try        {            StringWalker walker = CommandsManager.walkerFrom(SharedConstants.COMMAND_PREFIX, message);            commandsManager.handleCommandLine(walker, this);        }        catch (UnknownCommandException e)        {            trySendToServer(message); // Delegate command execution to server        }        catch (CommandException commandException)        {            clientUI.errorMessage(commandException.getMessage());        }    }    /**     * This method terminates the simplechat.client.     */    public void quit()    {        if (isConnected())        {            try            {                this.closeConnection();            }            catch (IOException e)            {                // Don't handle exception            }        }        System.exit(0);    }    @Override    protected void connectionClosed()    {        clientUI.infoMessage("Disconnected !");    }    @Override    protected void connectionException(Exception exception)    {        clientUI.infoMessage("Disconnected !");    }    public ChatIF getClientUI()    {        return clientUI;    }}