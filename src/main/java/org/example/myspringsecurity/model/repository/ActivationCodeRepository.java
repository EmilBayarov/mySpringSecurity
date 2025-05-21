package org.example.myspringsecurity.model.repository;

import org.example.myspringsecurity.model.entity.ActivationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivationCodeRepository extends JpaRepository<ActivationCode, Integer> {
    Optional<ActivationCode> findByCode(String code);
}
