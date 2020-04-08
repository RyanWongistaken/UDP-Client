package com.MaelStream.app;

import com.MaelStream.app.serverPackage.validateClass;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PackageUnitTest {

    @Test
    public void ipTest_isCorrect() {
        String ip = "192.168.1.76";

        validateClass validate = new validateClass();

        assertEquals(true, validate.IP(ip));
    }

    @Test
    public void ipTest_isIncorrect_Letters() {
        String ip = "afefawefafwe";

        validateClass validate = new validateClass();

        assertEquals(false, validate.IP(ip));
    }

    @Test
    public void ipTest_isIncorrect_Format() {
        String ip = "192.168.2554";

        validateClass validate = new validateClass();

        assertEquals(false, validate.IP(ip));
    }

    @Test
    public void portTest_isCorrect() {
        String port = "7855";

        validateClass validate = new validateClass();

        assertEquals(true, validate.Port(port));
    }

    @Test
    public void portTest_isIncorrect_letters() {
        String port = "785558846564a56sd456a45s6d4";

        validateClass validate = new validateClass();

        assertEquals(false, validate.Port(port));
    }

    @Test
    public void portTest_isIncorrect_invalidPort() {
        String port = "111111";

        validateClass validate = new validateClass();

        assertEquals(false, validate.Port(port));
    }


}