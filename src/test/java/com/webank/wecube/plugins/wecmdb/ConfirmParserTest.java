package com.webank.wecube.plugins.wecmdb;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.webank.plugins.wecmdb.exception.PluginException;
import com.webank.plugins.wecmdb.support.cmdb.ConfirmParser;

public class ConfirmParserTest {

    @Test
    public void whenParseSingleGuidShouldSuccess() {
        List<String> expected = ConfirmParser.parseGuid("0001_0000000001");
        assertEquals(1, expected.size());
        assertEquals("0001_0000000001", expected.get(0));
    }

    @Test
    public void whenParseMultiGuidShouldSuccess() {
        List<String> expected = ConfirmParser.parseGuid("[0001_0000000001,0001_0000000002]");
        assertEquals(expected.size(), 2);
        assertEquals("0001_0000000001", expected.get(0));
        assertEquals("0001_0000000002", expected.get(1));
    }

    @Test (expected = PluginException.class)
    public void whenParseMultiGuidWithInvalidFormatShouldExtractEmpty() {
        ConfirmParser.parseGuid("[0001_0000000001,0001_0000000002");
    }
}
