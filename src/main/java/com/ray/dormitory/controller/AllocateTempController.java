package com.ray.dormitory.controller;


import com.ray.dormitory.service.AllocateTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 宿舍预分配结果表 前端控制器
 * </p>
 *
 * @author Ray
 * @date 2020-3-23 21:10:14
 */
@RestController
@RequestMapping("/allocateTemps")
public class AllocateTempController {
    @Autowired
    private AllocateTempService allocateTempService;

}
