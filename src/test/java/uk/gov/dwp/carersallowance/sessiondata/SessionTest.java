package uk.gov.dwp.carersallowance.sessiondata;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by peterwhitehead on 23/12/2016.
 */
public class SessionTest {
    private Session session;

    @Before
    public void setUp() throws Exception {
        session = new Session("1234");
    }

    @Test
    public void testGetData() throws Exception {
        assertThat(session.getData().isEmpty(), is(true));
    }

    @Test
    public void testSetAttribute() throws Exception {
        session.setAttribute("test", "test1");
        assertThat(session.getData().get("test"), is("test1"));
    }

    @Test
    public void testGetAttribute() throws Exception {
        session.setAttribute("test", "test1");
        assertThat(session.getAttribute("test"), is("test1"));
    }

    @Test
    public void testGetAttributeNames() throws Exception {
        session.setAttribute("test", "test1");
        assertThat(session.getAttributeNames().get(0), is("test"));
    }

    @Test
    public void testRemoveAttribute() throws Exception {
        session.setAttribute("test", "test1");
        session.removeAttribute("test");
        assertThat(session.getData().isEmpty(), is(true));
    }
}