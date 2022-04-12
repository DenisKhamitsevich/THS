package com.market.ths.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
}
