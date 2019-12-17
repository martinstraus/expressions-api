/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
