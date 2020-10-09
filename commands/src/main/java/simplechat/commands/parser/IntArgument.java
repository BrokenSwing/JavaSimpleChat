package simplechat.commands.parser;

import static simplechat.commands.parser.ParsingResult.error;
import static simplechat.commands.parser.ParsingResult.ok;

/**
 * A class that provides static methods that returns different parsers for int arguments.
 */
public class IntArgument
{

    /**
     * Parses any value of integer.<br />
     *
     * @return the corresponding argument
     *
     * @see #positive()
     * @see #negative()
     * @see #between(int, int)
     */
    public static ArgumentParser<Integer> any()
    {
        return StringArgument.singleWord().then(intStr -> {
            try
            {
                return ok(Integer.parseInt(intStr));
            }
            catch (NumberFormatException e)
            {
                return error(intStr + " is not an integer.");
            }
        });
    }

    /**
     * Parses an integer positive value. A positive value is 0 or more.
     *
     * @return the corresponding parser
     */
    public static ArgumentParser<Integer> positive()
    {
        return any().then(v -> v < 0 ? error(v + " is not a positive value.") : ok(v));
    }

    /**
     * Parses an integer negative value. A negative value is 0 or less.
     *
     * @return the corresponding parser
     */
    public static ArgumentParser<Integer> negative()
    {
        return any().then(v -> v > 0 ? error(v + " is not a negative value.") : ok(v));
    }

    /**
     * Parses an integer n with <code>min <= n <= max</code>.
     *
     * @param min The minimum value for the parsed integer
     * @param max The maximum value for the parsed integer
     *
     * @throws IllegalArgumentException if min value is greater than max value
     *
     * @return the corresponding parser
     */
    public static ArgumentParser<Integer> between(int min, int max)
    {
        if (min > max)
        {
            throw new IllegalArgumentException("min value must be lower than or equal to max value");
        }
        return any().then(v -> {
            if (v < min)
            {
                return error(String.format("%d is lower than min value %d.", v, min));
            }
            if (v > max)
            {
                return error(String.format("%d is higher than max value %d.", v, max));
            }
            return ok(v);
        });
    }

    /* Disallow to create an instance of the class */
    private IntArgument() {}

}
