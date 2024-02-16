package uz.example.oasisuz.exception;


import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;
@Getter
public class CustomException extends RuntimeException {
    private final String message;
    private final HttpStatus httpStatus;
    private static final Logger logger = LoggerFactory.getLogger(CustomException.class);

    public CustomException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
        logger.error(message);
    }
    public CustomException(String message, Level level, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
        logCustomException(level);
    }

    private void logCustomException(Level level) {
        if (level == Level.ERROR) {
            logger.error(message);
        } else if (level == Level.DEBUG) {
            logger.debug(message);
        } else if (level == Level.INFO) {
            logger.info(message);
        } else if (level == Level.TRACE) {
            logger.trace(message);
        } else if (level == Level.WARN) {
            logger.warn(message);
        }
    }
}
