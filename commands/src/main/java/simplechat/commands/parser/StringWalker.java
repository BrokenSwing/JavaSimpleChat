package simplechat.commands.parser;

public class StringWalker
{

    private final String str;
    private int currentPosition = 0;

    public StringWalker(String str)
    {
        this.str = str;
    }

    public int getCurrentPosition()
    {
        return currentPosition;
    }

    public void setCurrentPosition(int newPosition)
    {
        if (newPosition > str.length())
        {
            throw new StringIndexOutOfBoundsException(newPosition);
        }
        currentPosition = newPosition;
    }

    public char getCurrentChar()
    {
        return str.charAt(currentPosition);
    }

    public char next()
    {
        return next(1);
    }

    public char next(int n)
    {
        char c = getCurrentChar();
        this.setCurrentPosition(this.currentPosition + n);
        return c;
    }

    public char previous()
    {
        return previous(1);
    }

    public char previous(int n)
    {
        this.setCurrentPosition(this.currentPosition - n);
        return getCurrentChar();
    }

    public char lookAhead()
    {
        return lookAhead(1);
    }

    public char lookAhead(int n)
    {
        return str.charAt(currentPosition + n);
    }

    public boolean hasNext()
    {
        return currentPosition < str.length();
    }

    public int skipWhileSpaces()
    {
        int count = 0;
        while (hasNext())
        {
            char ahead = next();
            if (ahead != ' ')
            {
                previous();
                break;
            }
            count++;
        }
        return count;
    }

}
