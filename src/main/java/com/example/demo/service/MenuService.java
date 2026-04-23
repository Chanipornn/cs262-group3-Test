package com.example.demo.service;

import com.example.demo.model.Menu;
import com.example.demo.repo.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
	private final MenuRepository repo;

    public MenuService(MenuRepository repo) {
        this.repo = repo;
    }

    // 🔍 SEARCH + FILTER
    public List<Menu> searchMenu(String q, Long categoryId) {

        List<Menu> menus = repo.findAll();

        return menus.stream()
                .filter(menu -> {

                    boolean matchName = (q == null || q.isEmpty())
                            || menu.getName().toLowerCase().contains(q.toLowerCase());

                    boolean matchCategory = (categoryId == null)
                            || menu.getCategoryId().equals(categoryId);

                    return matchName && matchCategory;
                })
                .toList();
    }

    public List<Menu> getAllMenu() {
        return repo.findAll();
    }

    public Menu getMenuById(Long id) {
        return repo.findById(id).orElse(null);
    }

}
