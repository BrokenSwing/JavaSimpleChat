package simplechat.client;import simplechat.common.ConsoleUI;import java.io.IOException;import java.util.concurrent.ThreadLocalRandom;public class ClientBoostrap{    /**     * The default port to connect on.     */    public static final int DEFAULT_PORT = 5555;    /**     * This method is responsible for the creation of the Client UI.     *     * @param args A [name, host, port] string array (optional).     */    public static void main(String[] args)    {        String name;        String host;        int port;        try        {            name = args[0];        }        catch (ArrayIndexOutOfBoundsException e)        {            name = "Speaker" + ThreadLocalRandom.current().nextInt(1000);        }        try        {            host = args[1];        }        catch (ArrayIndexOutOfBoundsException e)        {            host = "localhost";        }        try        {            port = Integer.parseInt(args[2]);        }        catch (ArrayIndexOutOfBoundsException e)        {            port = DEFAULT_PORT;        }        catch (NumberFormatException e)        {            throw new IllegalArgumentException("You must specify a integer for the port (second parameter)");        }        try        {            ConsoleUI chat = new ConsoleUI();            ChatClient client = new ChatClient(name, host, port, chat);            chat.setInputHandler(client::handleMessageFromClientUI);            chat.startQueryingInput();        }        catch (IOException e)        {            e.printStackTrace();        }    }}