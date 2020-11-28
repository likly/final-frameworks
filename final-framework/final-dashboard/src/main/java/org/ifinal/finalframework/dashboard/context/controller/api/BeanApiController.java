package org.ifinal.finalframework.dashboard.context.controller.api;

import org.ifinal.finalframework.dashboard.context.entity.BeanDefinition;
import org.ifinal.finalframework.dashboard.context.service.BeanService;
import org.ifinal.finalframework.dashboard.context.service.query.BeanQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/beans")
public class BeanApiController {

    @Resource
    private BeanService beanService;

    @GetMapping
    public List<BeanDefinition> query(BeanQuery query) {
        return beanService.query(query);
    }

}