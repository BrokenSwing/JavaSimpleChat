package simplechat.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class ConsoleUI implements ChatIF
{

    private Consumer<String> inputHandler;

    public ConsoleUI()
    {
        this(null);
    }

    public ConsoleUI(Consumer<String> inputHandler)
    {
        this.inputHandler = inputHandler;
    }

    public void setInputHandler(Consumer<String> inputHandler)
    {
        this.inputHandler = inputHandler;
    }

    @Override
    public void display(String message)
    {
        System.out.println(message);
    }

    @Override
    public void errorMessage(String message)
    {
        System.err.println(message);
    }

    public void startQueryingInput()
    {
        try
        {
            BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
            String message;

            while (true)
            {
                message = fromConsole.readLine();
                if (inputHandler != null)
                {
                    inputHandler.accept(message);
                }
            }
        }
        catch (Exception ex)
        {
            System.out.println("Unable to read from console!");
            ex.printStackTrace();
        }
    }

}
