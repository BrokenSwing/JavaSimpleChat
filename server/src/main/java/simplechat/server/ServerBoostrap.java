package simplechat.server;

import simplechat.common.ConsoleUI;

public class ServerBoostrap
{

    public static final int DEFAULT_PORT = 5555;

    /**
     * This method is responsible for the creation of
     * the server instance (there is no UI in this phase).
     *
     * @param args [port] The port number to listen on.  Defaults to 5555
     *             if no argument is entered.
     */
    public static void main(String[] args)
    {
        int port; //Port to listen on

        try
        {
            port = Integer.parseInt(args[0]); //Get port from command line
        }
        catch (Throwable t)
        {
            port = DEFAULT_PORT; //Set port to 5555
        }

        ConsoleUI ui = new ConsoleUI();
        EchoServer sv = new EchoServer(port, ui);
        ui.setInputHandler(sv::handleMessageFromUI);

        try
        {
            sv.listen(); //Start listening for connections
            ui.startQueryingInput();
        }
        catch (Exception ex)
        {
            System.out.println("ERROR - Could not listen for clients!");
        }
    }

}
