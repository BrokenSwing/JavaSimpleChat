package simplechat.commands.parser;

import java.util.StringJoiner;
import java.util.stream.Stream;

/**
 * A class providing static methods to parse String argument.
 */
public class StringArgument implements ArgumentParser<String>
{

    /* Disallow to construct a StringArgument, StringArgument::any() should be used instead */
    private StringArgument()
    {
    }

    /**
     * Parses a string that is delimited by two double-quotes.<br />
     * Expects the character a the current position of the walker to be a
     * double-quote.<br />
     * TODO: Handle double-quote escaping
     *
     * @param walker The walker to use to parse the string
     * @return the result of the parsing
     */
    private static ParsingResult<String> readDoubleQuoteDelimitedString(StringWalker walker)
    {
        walker.next(); // Skip first double-quote
        StringBuilder builder = new StringBuilder();
        char currentChar = ' ';
        while (walker.hasNext())
        {
            currentChar = walker.next();
            if (isDoubleQuote(currentChar))
            {
                break;
            }

            builder.append(currentChar);
        }

        if (isDoubleQuote(currentChar))
        {
            return ParsingResult.ok(builder.toString());
        }

        return ParsingResult.error("Expected '\"' but reached end of string.");
    }

    /**
     * Parses a string that is delimited by spaces.<br />
     * Expects the StringWalker not to have reached the end of the String.
     *
     * @param walker The walker to use to parse the String
     * @return the parsing result
     */
    private static ParsingResult<String> readSpaceDelimitedString(StringWalker walker)
    {
        StringBuilder builder = new StringBuilder();
        char currentChar;
        while (walker.hasNext())
        {
            currentChar = walker.next();
            if (isSpace(currentChar))
            {
                walker.previous(); // Don't consume space
                break;
            }

            builder.append(currentChar);
        }

        return ParsingResult.ok(builder.toString());
    }

    /**
     * @param c The character to test
     * @return true if c is a space, else false
     */
    private static boolean isSpace(char c)
    {
        return c == ' ';
    }

    /**
     * @param c The character to test
     * @return true if c is a double-quote, else false
     */
    private static boolean isDoubleQuote(char c)
    {
        return c == '"';
    }

    /**
     * Parses any string (space-delimited or double-quote delimited).
     *
     * @return the corresponding parser
     */
    public static ArgumentParser<String> any()
    {
        return new StringArgument();
    }

    /**
     * Parses any string (space-delimited or double-quotes delimited) then
     * checks if the parsed string is present in given values. <br />
     * Note: the check is case-insensitive
     *
     * @param values the acceptable values for the parsed string
     * @return the corresponding parser
     */
    public static ArgumentParser<String> oneOf(String... values)
    {
        if (values.length == 0)
        {
            throw new IllegalArgumentException("You can't provide empty choice set");
        }

        return new StringArgument().then(v ->
        {
            boolean found = Stream.of(values).anyMatch(v::equalsIgnoreCase);

            if (found)
            {
                return ParsingResult.ok(v);
            }

            StringJoiner joiner = new StringJoiner(", ");
            Stream.of(values).forEach(joiner::add);

            return ParsingResult.error(String.format(
                    "Expected one of %s, but got %s.",
                    joiner.toString(),
                    v
            ));
        });
    }

    /**
     * Parses a string that is delimited by spaces.
     *
     * @return the corresponding parser
     */
    public static ArgumentParser<String> singleWord()
    {
        return StringArgument::readSpaceDelimitedString;
    }

    @Override
    public ParsingResult<String> parse(StringWalker walker)
    {
        if (isDoubleQuote(walker.lookAhead())) // It's a double-quote delimited string
        {
            return readDoubleQuoteDelimitedString(walker);
        }
        return readSpaceDelimitedString(walker);
    }

}
