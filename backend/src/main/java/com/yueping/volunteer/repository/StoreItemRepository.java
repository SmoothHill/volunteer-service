package com.yueping.volunteer.repository;

import com.yueping.volunteer.model.StoreItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreItemRepository extends JpaRepository<StoreItem, Long> {

    List<StoreItem> findByActiveTrueOrderByPointsCostAsc();
}

