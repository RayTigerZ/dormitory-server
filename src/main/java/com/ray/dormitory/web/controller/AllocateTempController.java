package com.ray.dormitory.web.controller;


import com.ray.dormitory.infrastructure.entity.AllocateTemp;
import com.ray.dormitory.service.AllocateTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("")
    public boolean updateById(AllocateTemp allocateTemp) {
        Assert.notNull(allocateTemp.getId(), "参数错误");
        return allocateTempService.updateById(allocateTemp);
    }


}
