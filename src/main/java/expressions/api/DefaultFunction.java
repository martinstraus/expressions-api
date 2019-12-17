/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expressions.api;

import expressions.evaluator.SimpleEvaluator;
import java.util.Map;

/**
 *
 * @author martinstraus
 */
public class DefaultFunction implements Function {
    
    private final Function.Id id;
    private final String definition;

    public DefaultFunction(Function.Id id, String definition) {
        this.id = id;
        this.definition = definition;
    }

    @Override
    public Function.Id id() {
        return id;
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
