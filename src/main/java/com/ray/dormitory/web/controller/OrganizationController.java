package com.ray.dormitory.web.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ray.dormitory.bean.bo.OrganizationOption;
import com.ray.dormitory.bean.po.Organization;
import com.ray.dormitory.service.OrganizationService;
import com.ray.dormitory.system.SysConfig;
import com.ray.dormitory.upload.UploadDataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Ray
 * @date : 2019.11.28 19:46
 */
@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private SysConfig sysConfig;

    @GetMapping("")
    public List<Organization> list(@RequestParam(defaultValue = "3") int level) {
        return organizationService.list(level);
    }


    @PostMapping("")
    public boolean saveOrUpdate(@Valid Organization organization) {
        return organizationService.saveOrUpdate(organization);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable int id) {
        return organizationService.removeById(id);
    }

    @PostMapping("/batchSave")
    public boolean save(HttpServletRequest request, MultipartFile file) throws IOException {
        String token = request.getHeader(sysConfig.getTokenName());
        Assert.notNull(token, "token为空");

        EasyExcel.read(file.getInputStream(), Organization.class, new UploadDataListener<>(organizationService, token)).sheet().doRead();
        return true;
    }

    @GetMapping("/options")
    public List<OrganizationOption> getOptions() {
        Wrapper<Organization> wrapper = Wrappers.<Organization>lambdaQuery()
                .select(Organization::getId, Organization::getName)
                .isNull(Organization::getParentId)
                .orderByAsc(Organization::getCode);
        List<OrganizationOption> options = organizationService.list(wrapper).stream().map(OrganizationOption::convert).collect(Collectors.toList());
        options.forEach(i -> i.getChildren().forEach(j -> j.getChildren().forEach(k -> k.setChildren(null))));
        return options;
    }

}
