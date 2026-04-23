package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import com.example.demo.model.Menu;
import com.example.demo.repo.MenuRepository;
import com.example.demo.service.MenuService;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "*")
public class MenuController {
	/*
	 @Autowired
	    private MenuRepository repo;

	    // ดึงเมนูทั้งหมดจากฐานข้อมูล
	    @GetMapping
	    public List<Menu> getAllMenu() {
	        return repo.findAll();
	    }
	    // ดึงข้อมูลตาม id
	    @GetMapping("/{id}")
	    public Menu getMenuById(@PathVariable Long id) {
	        return repo.findById(id).orElse(null);
	    }
	    */

    private final MenuService service;

    public MenuController(MenuService service) {
        this.service = service;
    }

    // 🔍 SEARCH API
    @GetMapping("/search")
    public List<Menu> searchMenu(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId
    ) {
        return service.searchMenu(q, categoryId);
    }

    // 📄 GET ALL
    @GetMapping
    public List<Menu> getAllMenu() {
        return service.getAllMenu();
    }

    // 📄 GET BY ID
    @GetMapping("/{id}")
    public Menu getMenuById(@PathVariable Long id) {
        return service.getMenuById(id);
    }
}