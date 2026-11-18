package com.example.consultita.repository;

import com.example.consultita.entity.Seguro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISeguroRepository extends JpaRepository<Seguro, Integer> {
}
