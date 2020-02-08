package expressions.api;

/**
 *
 * @author martinstraus
 */
public interface Functions {

    Function create(Function.Id id, String definition);

    void update(Function.Id id, Function.Version version, String definition);

    Function findById(Function.Id id);

    Function findByIdAndVersion(Function.Id id, Function.Version version);

    int newEvaluation(Function function, String parameters);
}
