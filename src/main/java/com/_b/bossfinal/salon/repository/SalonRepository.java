package com._b.bossfinal.salon.repository;

import com._b.bossfinal.salon.entity.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Con JpaRepository ya tengo findAll y findById, que es lo unico que necesito para los salones
@Repository
public interface SalonRepository extends JpaRepository<Salon, Long> {
}
