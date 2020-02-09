package expressions.api;

import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 *
 * @author Martín Gaspar Straus <martinstraus@gmail.com>
 */
@Data
public class EvaluateRequest {

    private List<String> expressions;
    private Map<String, Object> parameters;
}
