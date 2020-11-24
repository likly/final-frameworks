package org.finalframework.example.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author likly
 * @version 1.0
 * @date 2020/11/23 15:01:17
 * @since 1.0
 */
@RestController
@RequestMapping("/api/list")
public class ListParamApiController {

    @GetMapping
    public List<String> list(@RequestParam("args") List<String> args) {
        return args;
    }


}
