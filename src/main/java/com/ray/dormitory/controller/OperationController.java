package com.ray.dormitory.controller;

import com.ray.dormitory.bean.po.Operation;
import com.ray.dormitory.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * @author : Ray
 * @date : 2020.02.14 15:46
 */
@RestController
@RequestMapping("/operations")
public class OperationController {
    @Autowired
    private OperationService operationService;

    @PostMapping("")
    public boolean saveOrUpdate(@Valid Operation operation) {
        return operationService.saveOrUpdate(operation);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id) {
        return operationService.removeById(id);
    }

}
