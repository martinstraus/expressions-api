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
