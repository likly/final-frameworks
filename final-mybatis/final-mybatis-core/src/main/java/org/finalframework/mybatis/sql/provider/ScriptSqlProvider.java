package org.finalframework.mybatis.sql.provider;


import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.Map;

/**
 * @author likly
 * @version 1.0
 * @date 2020-06-03 21:42:15
 * @since 1.0
 */
public interface ScriptSqlProvider extends SqlProvider {

    @Override
    default String provide(ProviderContext context, Map<String, Object> parameters) {
        final Logger logger = LoggerFactory.getLogger(context.getMapperType().getCanonicalName() + "." + context.getMapperMethod().getName());
        final XPathParser parser = new XPathParser("<script></script>");
        final XNode script = parser.evalNode("//script");
        doProvide(script.getNode(), script.getNode().getOwnerDocument(), parameters, context);
        final String sql = script.toString();
        if (logger.isDebugEnabled()) {
            final String[] sqls = sql.split("\n");
            for (String item : sqls) {
                logger.debug(item);
            }

            final XMLLanguageDriver driver = new XMLLanguageDriver();
            final SqlSource sqlSource = driver.createSqlSource(new Configuration(), script, null);
            final BoundSql boundSql = sqlSource.getBoundSql(parameters);
            logger.debug("sql ==> {}", boundSql.getSql());


        }
        return sql;
    }

    void doProvide(Node script, Document document, Map<String, Object> parameters, ProviderContext context);


}
