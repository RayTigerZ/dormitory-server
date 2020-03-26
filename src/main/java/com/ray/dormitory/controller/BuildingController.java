package com.ray.dormitory.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.bo.BuildingOption;
import com.ray.dormitory.bean.bo.Floor;
import com.ray.dormitory.bean.po.Building;
import com.ray.dormitory.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Ray
 * @date : 2019.11.22 14:54
 */
@RestController
@RequestMapping("/buildings")
public class BuildingController {
    @Autowired
    private BuildingService buildingService;

    @GetMapping("")
    public IPage<Building> getPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "100") int pageSize) {
        IPage<Building> page = new Page<>(pageNum, pageSize);
        return buildingService.page(page);
    }

    @PostMapping("")
    public boolean saveOrUpdate(@Valid Building building) {
        return buildingService.saveOrUpdate(building);
    }

    @GetMapping("/{buildingId}/floors")
    public List<Floor> getFloors(@PathVariable int buildingId) {
        return buildingService.getFloors(buildingId);
    }

    @GetMapping("/options")
    public List<BuildingOption> getOptions() {
        Wrapper<Building> wrapper = Wrappers.<Building>lambdaQuery()
                .select(Building::getId, Building::getName, Building::getType);
        return buildingService.list(wrapper).stream().map(BuildingOption::convert).collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable int id) {
        return buildingService.removeById(id);
    }
}
