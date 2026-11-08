package com.example.GameDepotAPI.repository;

import com.example.GameDepotAPI.model.videojuego.Videojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideojuegoRepository extends JpaRepository<Videojuego, Long> {

}
