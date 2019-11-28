/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expressions.api;

import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 *
 * @author martinstraus
 */
@Data
public class EvaluateRequest {

    private List<String> expressions;
    private Map<String, Object> parameters;
}
