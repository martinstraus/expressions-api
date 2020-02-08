package expressions.api;

import java.util.Map;

/**
 *
 * @author martinstraus
 */
public interface Function {

    public static class Id {

        private final String value;

        public Id(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

    }

    public static class Version {

        private final String value;

        public Version(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    Function.Id id();
    
    Function.Version version();

    String definition();

    Map<String, Object> evaluate(Map<String, Object> context);
}
