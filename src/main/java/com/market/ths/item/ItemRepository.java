package com.market.ths.item;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
    List<Item> findByPriceBetweenAndTitleContainingIgnoreCase(Double min, Double max, String title);
}
