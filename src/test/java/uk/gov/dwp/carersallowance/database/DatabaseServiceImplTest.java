package uk.gov.dwp.carersallowance.database;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by peterwhitehead on 22/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class DatabaseServiceImplTest {
    private DatabaseServiceImpl databaseServiceImpl;

    private final static String ORIGIN_TAG = "GB";

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private SimpleJdbcCall simpleJdbcCall;

    @Before
    public void setUp() throws Exception {
        when(simpleJdbcCall.withFunctionName(anyString())).thenReturn(simpleJdbcCall);
        when(simpleJdbcCall.withSchemaName(anyString())).thenReturn(simpleJdbcCall);
        when(simpleJdbcCall.withoutProcedureColumnMetaDataAccess()).thenReturn(simpleJdbcCall);
        when(simpleJdbcCall.declareParameters(Matchers.<SqlParameter>anyVararg())).thenReturn(simpleJdbcCall);
        when(simpleJdbcCall.withReturnValue()).thenReturn(simpleJdbcCall);
        databaseServiceImpl = new DatabaseServiceImpl(jdbcTemplate, simpleJdbcCall);
    }

    @Test
    public void testGetTransactionId() throws Exception {
        Map<String, Object> result = new ConcurrentHashMap<>();
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> transactions = new ConcurrentHashMap<>();
        transactions.put("result", "16100034567");
        list.add(transactions);
        result.put("#result-set-1", list);
        when(simpleJdbcCall.execute(Matchers.any(SqlParameterSource.class))).thenReturn(result);
        assertThat(databaseServiceImpl.getTransactionId(ORIGIN_TAG), is("16100034567"));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testGetTransactionIdFails() throws Exception {
        when(simpleJdbcCall.execute(Matchers.any(SqlParameterSource.class))).thenThrow(DataIntegrityViolationException.class);
        databaseServiceImpl.getTransactionId(ORIGIN_TAG);
    }
}