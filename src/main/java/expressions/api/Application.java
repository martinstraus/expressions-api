package expressions.api;

import expressions.evaluator.Evaluator;
import expressions.evaluator.SimpleEvaluator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Evaluator evaluator() {
        return new SimpleEvaluator();
    }

}
