package com.pool.poolcrud.Repository;

import com.pool.poolcrud.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsClientByEmail(String email);
}
