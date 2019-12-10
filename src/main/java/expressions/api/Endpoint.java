/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expressions.api;

import expressions.evaluator.Evaluator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author martinstraus
 */
@RestController
@RequestMapping("/expressions")
public class Endpoint {

    private final Evaluator evaluator;

    public Endpoint(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    @PostMapping("/evaluate")
    public List<Map<String, Object>> evaluate(@RequestBody EvaluateRequest request) {
        return request
                .getExpressions()
                .stream()
                .map((expression) -> result(expression, evaluator.evaluate(expression, request.getParameters())))
                .collect(toList());
    }

    private Map<String, Object> result(String expression, Map<String, Object> result) {
        Map<String, Object> map = new HashMap<>();
        map.put("expression", expression);
        map.putAll(result);
        return map;
    }

}
