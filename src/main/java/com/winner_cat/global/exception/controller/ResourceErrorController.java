package com.winner_cat.global.exception.controller;

import com.winner_cat.global.enums.statuscode.ErrorStatus;
import com.winner_cat.global.exception.GeneralException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceErrorController {
    @RequestMapping("/**")
    public void handleNotFound() {
        throw new GeneralException(ErrorStatus.RESOURCE_NOT_FOUND);
    }
}

