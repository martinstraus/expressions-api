package expressions.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Martín Gaspar Straus <martinstraus@gmail.com>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFound extends Exception {

    public NotFound(String message) {
        super(message);
    }

}
