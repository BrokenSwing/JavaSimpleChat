package simplechat.commands.parser;

import java.util.function.Function;

/**
 * A class that parses a String to the type T (if possible).
 *
 * @param <T> the target type
 */
public interface ArgumentParser<T>
{

    /**
     * Parses a string from the position of the received StringWalker.
     * The received walker isn't at the end position.
     * The first character isn't a space.
     *
     * @param walker The walker to use to parse the string
     * @return the parsed value
     */
    ParsingResult<T> parse(StringWalker walker);

    /**
     * Converts the underlying type if present
     *
     * @param converter The function that converts the underlying type
     * @param <C>       The new type the current value will be converted to
     * @return a new ArgumentParser for the new type
     */
    default <C> ArgumentParser<C> then(Function<T, ParsingResult<C>> converter)
    {
        return (reader) -> parse(reader).then(converter);
    }

}
