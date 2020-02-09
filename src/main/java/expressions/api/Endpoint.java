package expressions.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import expressions.evaluator.Evaluator;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Martín Gaspar Straus <martinstraus@gmail.com>
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
    public void create(@PathVariable String id, @RequestBody NewFunction request) {
        functions.create(new Function.Id(id), request.getDefinition());
    }

    @PostMapping("/functions/{id}/evaluations")
    public ResponseEntity<Map<String, Object>> evaluate(
        @PathVariable String id,
        @RequestBody Map<String, Object> request
    ) throws NotFound, URISyntaxException, JsonProcessingException {
        Function function = functions.findById(new Function.Id(id));
        if (function == null) {
            throw new NotFound(String.format("Function %s not found.", id));
        }
        Map<String, Object> result = function.evaluate(request);
        int evaluationId = functions.newEvaluation(function, new ObjectMapper().writeValueAsString(request));
        return new ResponseEntity<>(
            result(function.definition(), result),
            locationHeader(String.format("/functions/%s/evaluations/%d", id, evaluationId)),
            HttpStatus.CREATED
        );
    }

    @PostMapping("/functions/{id}/versions/{version}/evaluations")
    public ResponseEntity<Map<String, Object>> evaluate(
        @PathVariable String id,
        @PathVariable String version,
        @RequestBody Map<String, Object> request
    ) throws NotFound, URISyntaxException, JsonProcessingException {
        Function function = functions.findById(new Function.Id(id));
        if (function == null) {
            throw new NotFound(String.format("Function %s not found.", id));
        }
        Map<String, Object> result = function.evaluate(request);
        int evaluationId = functions.newEvaluation(function, new ObjectMapper().writeValueAsString(request));
        return new ResponseEntity<>(
            result(function.definition(), result),
            locationHeader(String.format("/functions/%s/evaluations/%d", id, evaluationId)),
            HttpStatus.CREATED
        );
    }

    @PostMapping("/functions/{id}/versions/{version}")
    public void update(
        @PathVariable String id,
        @PathVariable String version,
        @RequestBody NewFunction request
    ) throws NotFound, URISyntaxException, JsonProcessingException {
        functions.update(new Function.Id(id), new Function.Version(version), request.getDefinition());
    }

    private HttpHeaders locationHeader(String value) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(value));
        return headers;
    }

    private Map<String, Object> result(String expression, Map<String, Object> result) {
        Map<String, Object> map = new HashMap<>();
        map.put("expression", expression);
        map.putAll(result);
        return map;
    }

}
