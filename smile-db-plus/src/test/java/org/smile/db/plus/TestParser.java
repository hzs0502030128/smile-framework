package org.smile.db.plus;

import org.junit.Test;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

/**
 * Unit test for simple App.
 */
public class TestParser 
{
    /**
     * Rigorous Test :-)
     * @throws JSQLParserException 
     */
    @Test
    public void testParser() throws JSQLParserException
    {
        String sql="select * from wms_nam where name like :name";
        Statement statement=CCJSqlParserUtil.parse(sql);
        System.out.println(statement.toString());
    }
}
