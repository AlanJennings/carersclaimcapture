package uk.gov.dwp.carersallowance.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseServiceImpl.class);

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

    @Override
    public String getTransactionStatusById(final String transactionId) {
        return jdbcTemplate.query("SELECT status FROM transactionstatus WHERE transaction_id = ?", resultSet -> resultSet.next() ? resultSet.getString(1) : "", transactionId);
    }

    @Override
    public Boolean health() {
        return jdbcTemplate.query("SELECT 1", resultSet -> resultSet.next() ? true : false);
    }

    @Override
    public Boolean setTransactionStatusById(final String transactionId, final String status) {
        final String updateSQL = "UPDATE transactionstatus SET status = ? WHERE transaction_id = ?";
        final Object[] args = new Object[]{ transactionId, status };
        final int stored = jdbcTemplate.update(updateSQL, args);
        Boolean rtn = Boolean.TRUE;
        if (stored <= 0) {
            LOG.error("Could not update status:{} into transactionstatus for transactionId:{}.", status, transactionId);
            rtn = Boolean.FALSE;
        } else {
            LOG.debug("Successfully update status:{} into transactionstatus for transactionId:{}.", status, transactionId);
        }
        return rtn;
    }

    @Override
    public Boolean insertTransactionStatus(final String transactionId, final String status, final Integer type, final Integer thirdParty,
                                           final Integer circsType, final String lang, final Integer jsEnabled, final Integer email,
                                           final Integer saveForLaterEmail, final String originTag) {
        final String insertSQL = "INSERT INTO transactionstatus(transaction_id, status, type, thirdparty, circs_type, lang, js_enabled, email, saveforlateremail, origintag) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        final Object[] args = new Object[]{ transactionId, status, type, thirdParty, circsType, lang, jsEnabled, email, saveForLaterEmail, originTag };
        final int stored = jdbcTemplate.update(insertSQL, args);
        Boolean rtn = Boolean.TRUE;
        if (stored <= 0) {
            LOG.error("Could not insert into transactionstatus for transactionId:{}.", transactionId);
            rtn = Boolean.FALSE;
        } else {
            LOG.debug("Successfully inserted into transactionstatus for transactionId:{}.", transactionId);
        }
        return rtn;
    }
}
