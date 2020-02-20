package com.ray.dormitory.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ray.dormitory.bean.bo.OrganizationOption;
import com.ray.dormitory.bean.po.Organization;
import com.ray.dormitory.service.OrganizationService;
import com.ray.dormitory.util.UploadDataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.28 19:46
 */
@RestController
@RequestMapping("/organizations")
public class OrganizationController {
    private static int[] values = {3, 5, 10};
    @Autowired
    private OrganizationService organizationService;

    @GetMapping("")//debugging
    public List<Organization> list(Integer level) {

        List<Organization> list;
        if (level == null) {
            list = organizationService.list(Wrappers.<Organization>lambdaQuery().isNull(Organization::getParentId));
            //将组织树结构的第三层 children属性设置为null，方便前端渲染
            for (Organization a : list) {
                for (Organization b : a.getChildren()) {
                    for (Organization c : b.getChildren()) {
                        c.setChildren(null);
                    }
                }
            }
        } else {
            //获取指定层级的组织"length(code)"
            list = organizationService.list(Wrappers.<Organization>lambdaQuery().eq(Organization::getCode, values[level - 1]).orderByAsc(Organization::getCode));
        }

        return list;
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
    public boolean save(String time, MultipartFile file) throws IOException {
        UploadDataListener<Organization> listener = new UploadDataListener<>(organizationService, time);
        EasyExcel.read(file.getInputStream(), Organization.class, listener).sheet().doRead();
        return true;
    }

    @GetMapping("/options")
    public List<OrganizationOption> getOptions() {
        List<OrganizationOption> options = organizationService.getOptions();
        for (OrganizationOption a : options) {
            for (OrganizationOption b : a.getChildren()) {
                for (OrganizationOption c : b.getChildren()) {
                    c.setChildren(null);
                }
            }
        }
        return options;
    }

}
