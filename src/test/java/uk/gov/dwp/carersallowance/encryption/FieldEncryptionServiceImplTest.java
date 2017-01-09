package uk.gov.dwp.carersallowance.encryption;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by peterwhitehead on 04/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class FieldEncryptionServiceImplTest {
    private FieldEncryptionServiceImpl fieldEncryptionService;

    @Before
    public void setUp() throws Exception {
        fieldEncryptionService = new FieldEncryptionServiceImpl("8a20d772-b998-486d-ba05-fd3ef75d4fd2", true);
    }

    @Test
    public void testEncryptAES() throws Exception {
        assertThat(fieldEncryptionService.encryptAES("test"), is(not("test")));
    }

    @Test
    public void testDecryptAES() throws Exception {
        assertThat(fieldEncryptionService.decryptAES("5B4A73B4EB822ED61A9857703A74C9ED"), is(not("test")));
    }
}