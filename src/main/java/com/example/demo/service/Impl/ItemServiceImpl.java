package com.example.demo.service.Impl;

import com.example.demo.entities.Item;
import com.example.demo.exception.ItemNotFoundException;
import com.example.demo.repositories.ItemRepository;
import com.example.demo.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Item getItem(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + id));
    }
}
