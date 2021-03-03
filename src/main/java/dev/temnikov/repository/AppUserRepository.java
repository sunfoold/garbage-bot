package dev.temnikov.repository;

import dev.temnikov.domain.AppUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AppUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByTelegramChatId(Long aLong);

    Optional<AppUser> findByPhoneNumber(String phoneNumber);
}
