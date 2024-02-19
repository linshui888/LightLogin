package top.cmarco.lightlogin.log;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilter;


public final class SafetyFilter extends AbstractFilter {

    @Override
    public Result filter(final LogEvent event) {
        final String message = event.getMessage().getFormattedMessage();

        if (message.contains("/register") || message.contains("/login") || message.contains("/changepassword")) {
            return Result.DENY;
        }

        return Result.NEUTRAL;
    }
}
