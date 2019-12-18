/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expressions.api;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author martinstraus
 */
@Component
public class DatabaseFunctions implements Functions {

    private static final String SELECT_LATEST_VERSION = "select functions.id, functions.version, functions_versions.definition"
        + " from"
        + "    functions_versions"
        + "    join functions on functions.id = functions_versions.id and functions.version = functions_versions.version"
        + " where functions.id = ?";
    private static final String INSERT_FUNCTION = "insert into functions (id) values (?)";
    private static final String INSERT_FUNCTION_VERSION = "insert into functions_versions (id, version, definition) values (?,?,?)";
    private static final String UPDATE_FUNCTION_VERSION = "update functions set version = ? where id = ?";
    private static final String SELECT_VERSION_FOR_UPDATE = "select id from functions where id = ? for update";
    private static final String SELECT_NEXT_EXECUTION_ID = "select next_value from evaluations_ids where function = ?";
    private static final String INSERT_EVALUATION = "insert into evaluations (function, version, id, parameters) values (?,?,?,?::json)";
    private final JdbcTemplate jdbc;

    public DatabaseFunctions(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    @Override
    public Function create(Function.Id id, String definition) {
        String version = "0.0.1";
        jdbc.update(INSERT_FUNCTION, id.value());
        jdbc.update(INSERT_FUNCTION_VERSION, id.value(), version, definition);
        jdbc.update(UPDATE_FUNCTION_VERSION, version, id.value());
        return new DefaultFunction(id, new Function.Version(version), definition);
    }

    @Override
    public Function findById(Function.Id id) {
        List<Function> result = jdbc.query(SELECT_LATEST_VERSION, this::transform, id.value());
        return result.size() == 1 ? result.get(0) : null;
    }

    private Function transform(ResultSet rs, int row) throws SQLException {
        return new DefaultFunction(
            new Function.Id(rs.getString("id")),
            new Function.Version(rs.getString("version")),
            rs.getString("definition")
        );
    }

    @Transactional
    @Override
    public int newEvaluation(Function function, String parameters) {
        lock(function.id());
        int id = nextExecutionId(function);
        jdbc.update(INSERT_EVALUATION, function.id().value(), function.version().value(), id, parameters);
        return id;
    }

    private int nextExecutionId(Function function) {
        try {
            return jdbc.queryForObject(SELECT_NEXT_EXECUTION_ID, this::transformNextId, function.id().value());
        } catch (EmptyResultDataAccessException ex) {
            return 1;
        }
    }

    private Integer transformNextId(ResultSet rs, int row) throws SQLException {
        return rs.isLast() ? rs.getInt("next_value") : 1;
    }

    private void lock(Function.Id id) {
        jdbc.query(SELECT_VERSION_FOR_UPDATE, (rs, row) -> null, id.value());
    }

}
