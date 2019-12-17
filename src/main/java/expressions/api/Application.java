package expressions.api;

import expressions.evaluator.Evaluator;
import expressions.evaluator.SimpleEvaluator;
import expressions.parser.Parser;
import expressions.parser.antlr.AntlrParser;
import javax.sql.DataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

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

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource(
            "jdbc:postgresql://localhost:5432/postgres?search_path=expressions", 
            "expressions", 
            "expressions"
        );
        ds.setDriverClassName("org.postgresql.Driver");
        return ds;
    }
    
    @Bean
    public Parser parser() {
        return new AntlrParser();
    }

}
