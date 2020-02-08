package expressions.api;

import expressions.evaluator.SimpleEvaluator;
import java.util.Map;

/**
 *
 * @author martinstraus
 */
public class DefaultFunction implements Function {
    
    private final Function.Id id;
    private final Function.Version version;
    private final String definition;

    public DefaultFunction(Function.Id id,  Function.Version version, String definition) {
        this.id = id;
        this.version = version;
        this.definition = definition;
    }

    @Override
    public Function.Id id() {
        return id;
    }

    @Override
    public Version version() {
        return version;
    }

    @Override
    public String definition() {
        return definition;
    }

    @Override
    public Map<String, Object> evaluate(Map<String, Object> context) {
        return new SimpleEvaluator().evaluate(definition, context);
    }
    
}
