package org.finalframework.spiriter.jdbc.api.controller;

import java.util.List;
import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.finalframework.spiriter.jdbc.dao.mapper.CommonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author likly
 * @version 1.0
 * @date 2020-05-20 20:44:42
 * @since 1.0
 */
@RestController
@RequestMapping("/api/jdbc")
public class CommonApiController implements InitializingBean {

    public static final Logger logger = LoggerFactory.getLogger(CommonApiController.class);

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    private CommonMapper commonMapper;

    @GetMapping("/databases")
    public List<String> databases() {
        return commonMapper.showDatabases();
    }

    @GetMapping("/database")
    public String database() {
        return commonMapper.selectDatabase();
    }

    @GetMapping("/version")
    public String version() {
        return commonMapper.selectVersion();
    }

    @GetMapping("/showTables")
    public List<String> showTables() {
        return commonMapper.showTables();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.commonMapper = sqlSessionFactory.getConfiguration()
                .getMapper(CommonMapper.class, sqlSessionFactory.openSession());

    }
}

