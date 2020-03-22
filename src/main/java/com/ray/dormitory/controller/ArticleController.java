package com.ray.dormitory.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.po.Article;
import com.ray.dormitory.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
}
