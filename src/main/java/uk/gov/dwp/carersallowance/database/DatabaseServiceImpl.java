package uk.gov.dwp.carersallowance.database;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by peterwhitehead on 22/12/2016.
 */
@Component
public class DatabaseServiceImpl implements DatabaseService {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall simpleJdbcCall;

    @Inject
    public DatabaseServiceImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
    }

    public DatabaseServiceImpl(final JdbcTemplate jdbcTemplate, final SimpleJdbcCall simpleJdbcCall) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcCall = simpleJdbcCall;
    }

    @Override
    public String getTransactionId(final String originTag) {
        simpleJdbcCall
                .withFunctionName("get_new_transaction_id")
                .withSchemaName("public")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(new SqlParameter("origintag", Types.VARCHAR))
                .withReturnValue();
        SqlParameterSource params = new MapSqlParameterSource().addValue("origintag", originTag);
        Map<String, Object> result = simpleJdbcCall.execute(params);
        ArrayList<Map<String, String>> list = (ArrayList<Map<String, String>>)result.get("#result-set-1");
        String transactionId = list.get(0).get("result");
        return transactionId;
    }
}
