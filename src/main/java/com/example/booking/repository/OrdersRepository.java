package com.example.booking.repository;

import com.example.booking.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrdersRepository extends PagingAndSortingRepository<Order, Long>, CrudRepository<Order, Long> {
    List<Order> findOrdersByUser_Id(long userId);
    List<Order> findOrdersByRoom_Id(long roomId);
    void deleteById(long id);
}
