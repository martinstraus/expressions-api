/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expressions.api;

import expressions.evaluator.Evaluator;
import expressions.parser.Parser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author martinstraus
 */
@RestController
public class Endpoint {

    private final Evaluator evaluator;
    private final Functions functions;

    public Endpoint(Evaluator evaluator, Functions functions) {
        this.evaluator = evaluator;
        this.functions = functions;
    }

    @PostMapping("/expressions/evaluate")
    public List<Map<String, Object>> evaluate(@RequestBody EvaluateRequest request) {
        return request
                .getExpressions()
                .stream()
                .map((expression) -> result(expression, evaluator.evaluate(expression, request.getParameters())))
                .collect(toList());
    }

    @PostMapping("/functions/{id}")
    public void evaluate(@PathVariable String id, @RequestBody NewFunction request) {
        functions.create(new Function.Id(id), request.getDefinition());
    }
    
    @PostMapping("/functions/{id}/evaluation")
    public Map<String, Object> evaluate(@PathVariable String id, @RequestBody Map<String, Object> request) throws NotFound {
        Function function = functions.findById(new Function.Id(id));
        if (function == null) {
            throw new NotFound(String.format("Function %s not found.", id));
        }
        return result(function.definition(), function.evaluate(request));
    }

    private Map<String, Object> result(String expression, Map<String, Object> result) {
        Map<String, Object> map = new HashMap<>();
        map.put("expression", expression);
        map.putAll(result);
        return map;
    }

}
