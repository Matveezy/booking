package com.example.booking.repository;

import com.example.booking.entity.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends PagingAndSortingRepository<Order, Long>, CrudRepository<Order, Long> {
    List<Order> findOrdersByUser_Id(long userId);
    List<Order> findOrdersByRoom_Id(long roomId);

    Optional<Order> findById(long id);

    void deleteById(long id);
}
