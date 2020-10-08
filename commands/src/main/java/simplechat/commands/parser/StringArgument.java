package simplechat.commands.parser;

import java.util.StringJoiner;
import java.util.stream.Stream;

public class StringArgument implements ArgumentParser<String>
{

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

    private static ParsingResult<String> readSpaceDelimitedString(StringWalker walker)
    {
        StringBuilder builder = new StringBuilder();
        char currentChar;
        while (walker.hasNext())
        {
            currentChar = walker.next();
            if (isSpace(currentChar))
            {
                break;
            }

            builder.append(currentChar);
        }

        walker.previous(); // Don't consume space
        return ParsingResult.ok(builder.toString());
    }

    private static boolean isSpace(char c)
    {
        return c == ' ';
    }

    private static boolean isDoubleQuote(char c)
    {
        return c == '"';
    }

    public static ArgumentParser<String> any()
    {
        return new StringArgument();
    }

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
