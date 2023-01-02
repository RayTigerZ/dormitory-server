package com.ray.dormitory.web.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.bo.ArticleTypeOption;
import com.ray.dormitory.bean.po.ArticleType;
import com.ray.dormitory.service.ArticleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章类型 前端控制器
 * </p>
 *
 * @author Ray
 * @date 2020-1-10 15:27:19
 */
@RestController
@RequestMapping("/articleTypes")
public class ArticleTypeController {
    @Autowired
    private ArticleTypeService articleTypeService;

    @GetMapping("")
    public IPage<ArticleType> getPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        IPage<ArticleType> page = new Page<>(pageNum, pageSize);
        return articleTypeService.page(page);
    }

    @PostMapping("")
    public boolean saveOrUpdate(@Valid ArticleType articleType) {
        return articleTypeService.saveOrUpdate(articleType);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable int id) {
        return articleTypeService.removeById(id);
    }

    @GetMapping("/options")
    public List<ArticleTypeOption> getOptions() {
        Wrapper<ArticleType> wrapper = Wrappers.<ArticleType>lambdaQuery()
                .select(ArticleType::getId, ArticleType::getName);
        return articleTypeService.list(wrapper).stream().map(ArticleTypeOption::convert).collect(Collectors.toList());
    }
}
