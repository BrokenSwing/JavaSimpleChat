package simplechat.commands.parser;

/**
 * A class that allows to iterate on String characters, moving forward and backward.
 * It also keep track of the current position and allows to jump directly to a given position.
 */
public class StringWalker
{

    private final String str;
    private int currentPosition = 0;

    /**
     * Constructs a StringWalker that will walk on the given String.
     * @param str the String to walk on
     */
    public StringWalker(String str)
    {
        this.str = str;
    }

    /**
     * @return current position of the cursor on the String
     */
    public int getCurrentPosition()
    {
        return currentPosition;
    }

    /**
     * Set the position of the cursor on the underlying String.
     *
     * @param newPosition The new position of the cursor
     * @throws StringIndexOutOfBoundsException if the newPosition is higher than the length of the underlying String
     */
    public void setCurrentPosition(int newPosition)
    {
        if (newPosition > str.length())
        {
            throw new StringIndexOutOfBoundsException(newPosition);
        }
        currentPosition = newPosition;
    }

    /**
     * @return the character of the current position of the cursor
     */
    public char getCurrentChar()
    {
        return str.charAt(currentPosition);
    }

    /**
     * Returns the character at the current position then moves the cursor
     * by one forward.
     * @return the character at the current position
     */
    public char next()
    {
        return next(1);
    }

    /**
     * Returns the character at the current position then moves the cursor
     * by n forward.
     *
     * @param n The amount of characters to move forward
     * @return the character at the current position
     */
    public char next(int n)
    {
        char c = getCurrentChar();
        this.setCurrentPosition(this.currentPosition + n);
        return c;
    }

    /**
     * Moves the cursor backward then returns the character at the new cursor position.
     *
     * @return the character at the previous position
     */
    public char previous()
    {
        return previous(1);
    }

    /**
     * Moves the character backward by n then returns the character at the new cursor position.
     *
     * @param n The amount of characters to move backward
     * @return the character of the previous position
     */
    public char previous(int n)
    {
        this.setCurrentPosition(this.currentPosition - n);
        return getCurrentChar();
    }

    /**
     * Returns the character at the position of the cursor + 1
     * but does not move the cursor.
     * @return the character of the position of the cursor + 1
     */
    public char lookAhead()
    {
        return lookAhead(1);
    }

    /**
     * Returns the character at the position of the cursor + n
     * but does not move the cursor.
     * @param n The amount of character to look ahead
     * @return the character of the position of the cursor + n
     */
    public char lookAhead(int n)
    {
        return str.charAt(currentPosition + n);
    }

    /**
     * @return true if the cursor isn't at the end of the string, else false
     */
    public boolean hasNext()
    {
        return currentPosition < str.length();
    }

    /**
     * Moves the cursor forward until the current character isn't a space.
     *
     * @return the number of times the cursor moved forward
     */
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
