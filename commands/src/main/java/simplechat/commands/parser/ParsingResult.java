package simplechat.commands.parser;

import java.util.function.Function;

/**
 * Result of a call of {@link ArgumentParser#parse(StringWalker)}.
 * It can contain the parsed value (if it is the case, the result is considered as "ok") or contain
 * an error message (if it is the case, the result is considered as "non-ok" or "errored").
 *
 * @param <T> The type of the parsed value
 */
public class ParsingResult<T>
{

    private final T value;
    private final String errorMessage;

    private ParsingResult(T value, String errorMessage)
    {
        this.value = value;
        this.errorMessage = errorMessage;
    }

    /**
     * Constructs a result that is ok.
     *
     * @param value The parsed value
     * @param <T>   The type of the parsed value
     * @return an "ok" parsing result
     */
    public static <T> ParsingResult<T> ok(T value)
    {
        return new ParsingResult<>(value, null);
    }

    /**
     * Constructs a result that is errored (or non-ok).
     *
     * @param errorMessage A message describing the problem encountered while parsing
     * @param <T>          The type of the parsed value
     * @return an "errored" parsing value
     */
    public static <T> ParsingResult<T> error(String errorMessage)
    {
        return new ParsingResult<>(null, errorMessage);
    }

    /**
     * Indicates whether or not the parsing performed without error.
     *
     * @return true if the parsing performed without error, else false
     */
    public boolean isOk()
    {
        return errorMessage == null;
    }

    /**
     * Returns the parsed value. This value can be null even if {@link #isOk()} returns
     * true. If {@link #isOk()} returns false, then this method returns null.
     *
     * @return the parsed value or null
     */
    public T getValue()
    {
        return value;
    }

    /**
     * Returns a message describing the reason the parsing failed. If {@link #isOk()} returns
     * true, this method returns null, else this method returns a String.
     *
     * @return the error message or null
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }

    /**
     * Maps the parsed value to an other value.<br />
     * <p>
     * If <code>this</code> is errored, the converter isn't called and an errored
     * {@link ParsingResult<C>} is returned with the same error message as <code>this</code><br />.
     * <p>
     * If <code>this</code> is ok, the converter is applied to the parsed value and {@link ParsingResult<C>}
     * returned by the converter is returned from the function.
     *
     * @param converter A function that maps the parsed value to a new ParsingResult
     * @param <C>       The target type of the mapping
     * @return a ParsingResult with a generic type corresponding to the target type of the mapping
     */
    public <C> ParsingResult<C> then(Function<T, ParsingResult<C>> converter)
    {
        if (isOk())
        {
            return converter.apply(this.value);
        }
        return error(errorMessage);
    }

}
