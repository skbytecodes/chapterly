package com.chapterly.service;

import com.chapterly.entity.OrderedItem;
import com.chapterly.repository.OrderedItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderedItemService {

    @Autowired
    private OrderedItemRepo orderedItemRepo;

    public OrderedItem saveOrderedItem(OrderedItem item){
        try {
            return orderedItemRepo.save(item);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            throw new IllegalArgumentException("Given entity is null");
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving OrderedItem");
        }
    }
}
