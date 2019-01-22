package com.boying.cpapi.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.lottery.common.utils.R;

/**
 * 测试接口
 *
 * @author 
 * @email 
 * @date 2017-03-23 15:47
 */
@RestController
public class ApiTestController {
    @GetMapping("test")
    public R userInfo(){
        return R.ok();
    }

}
