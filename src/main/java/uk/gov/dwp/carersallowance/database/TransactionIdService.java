package uk.gov.dwp.carersallowance.database;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class TransactionIdService {
    private final JdbcTemplate jdbcTemplate;
    private final String originTag;

    @Inject
    public TransactionIdService(final JdbcTemplate jdbcTemplate, @Value("${origin.tag}") final String originTag) {
        this.jdbcTemplate = jdbcTemplate;
        this.originTag = originTag;
    }

    public String getTransactionId() {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
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