package com.ray.dormitory.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
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

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("")
    public List<Organization> list(Integer level) {

        if (level == null) {
            Wrapper<Organization> wrapper = Wrappers.<Organization>lambdaQuery()
                    .isNull(Organization::getParentId)
                    .orderByAsc(Organization::getCode);
            return organizationService.list(wrapper);
        } else {
            //获取指定层级的组织"length(code)"

            return organizationService.level(level);
        }

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
        return organizationService.getOptions();
    }

}
