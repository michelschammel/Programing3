package factories;

import models.Quote;
import models.interfaces.QuoteInterface;

public abstract class QuoteFactory {

    public static QuoteInterface createQuote() {
        return new Quote();
    }
}
