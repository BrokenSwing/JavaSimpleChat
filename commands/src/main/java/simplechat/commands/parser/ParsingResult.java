package simplechat.commands.parser;

import java.util.function.Function;

public class ParsingResult<T>
{

    private final T value;
    private final String errorMessage;

    private ParsingResult(T value, String errorMessage)
    {
        this.value = value;
        this.errorMessage = errorMessage;
    }

    public static <T> ParsingResult<T> ok(T value)
    {
        return new ParsingResult<>(value, null);
    }

    public static <T> ParsingResult<T> error(String errorMessage)
    {
        return new ParsingResult<>(null, errorMessage);
    }

    public boolean isOk()
    {
        return errorMessage == null;
    }

    public T getValue()
    {
        return value;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public <C> ParsingResult<C> then(Function<T, ParsingResult<C>> converter)
    {
        if (isOk())
        {
            return converter.apply(this.value);
        }
        return error(errorMessage);
    }

}
