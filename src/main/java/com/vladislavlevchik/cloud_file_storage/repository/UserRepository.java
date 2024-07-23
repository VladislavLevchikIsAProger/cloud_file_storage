package com.vladislavlevchik.cloud_file_storage.repository;

import com.vladislavlevchik.cloud_file_storage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

}
