package com.example.GameDepotAPI.repository;

import com.example.GameDepotAPI.model.orden.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
}
