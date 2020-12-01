package org.ifinal.finalframework.example.web.controller;

import org.ifinal.finalframework.example.entity.Person;
import org.ifinal.finalframework.example.service.PersonService;
import org.ifinal.finalframework.monitor.annotation.MonitorAction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author likly
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Resource
    private PersonService personService;

    @GetMapping
    @MonitorAction("查询Person")
    public List<Person> query() {
        return personService.select();
    }

}
