package expressions.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author martinstraus
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFound extends Exception {

    public NotFound(String message) {
        super(message);
    }

}
