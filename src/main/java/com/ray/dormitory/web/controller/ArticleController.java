package com.ray.dormitory.web.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.bo.ArticleTypeOption;
import com.ray.dormitory.infrastructure.entity.Article;
import com.ray.dormitory.infrastructure.entity.ArticleType;
import com.ray.dormitory.service.ArticleService;
import com.ray.dormitory.service.ArticleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章 前端控制器
 * </p>
 *
 * @author Ray
 * @date 2020-1-18 17:37:28
 */
@RestController
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleTypeService articleTypeService;

    @GetMapping("")
    public IPage<Article> getPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, String title, String articleTypeId) {
        IPage<Article> page = new Page<>(pageNum, pageSize);
        Wrapper<Article> wrapper = Wrappers.<Article>lambdaQuery()
                .like(StringUtils.isNotBlank(title), Article::getTitle, title)
                .eq(StringUtils.isNotBlank(articleTypeId), Article::getArticleTypeId, articleTypeId)
                .orderByDesc(Article::getCreateTime);
        return articleService.page(page, wrapper);
    }

    @PostMapping("")
    public boolean saveOrUpdate(@Valid Article article) {
        return articleService.saveOrUpdate(article);
    }

    @GetMapping("/{id}")
    public Article getById(@PathVariable int id) {
        return articleService.getById(id);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable int id) {
        return articleService.removeById(id);
    }

    @GetMapping("/indexShow")
    public Map<String, Object> indexShow() {
        Map<String, Object> map = new HashMap<>(2);
        List<ArticleTypeOption> types = articleTypeService.list(Wrappers.<ArticleType>lambdaQuery().eq(ArticleType::getIsShow, true)).stream().map(ArticleTypeOption::convert).collect(Collectors.toList());

        map.put("types", types);
        List<List<Article>> newsList = new ArrayList<>();
        types.forEach(type -> {
            List<Article> list = articleService.page(new Page<>(1, 5), Wrappers.<Article>lambdaQuery().eq(Article::getArticleTypeId, type.getId())).getRecords();
            newsList.add(list);

        });
        map.put("newsList", newsList);
        return map;
    }

    @GetMapping("/all")
    public Map<String, Object> all() {
        Map<String, Object> map = new HashMap<>(2);
        List<ArticleTypeOption> types = articleTypeService.list(Wrappers.<ArticleType>lambdaQuery().eq(ArticleType::getIsShow, true)).stream().map(ArticleTypeOption::convert).collect(Collectors.toList());

        map.put("types", types);
        List<List<Article>> newsList = new ArrayList<>();
        types.forEach(type -> {
            List<Article> list = articleService.list(Wrappers.<Article>lambdaQuery().eq(Article::getArticleTypeId, type.getId()));
            newsList.add(list);

        });
        map.put("newsList", newsList);
        return map;
    }
}
