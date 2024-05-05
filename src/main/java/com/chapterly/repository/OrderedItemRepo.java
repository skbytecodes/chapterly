package com.chapterly.repository;

import com.chapterly.entity.OrderedItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedItemRepo extends JpaRepository<OrderedItem, Long> {
}
