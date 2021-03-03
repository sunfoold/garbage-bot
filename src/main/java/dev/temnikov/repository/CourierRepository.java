package dev.temnikov.repository;

import dev.temnikov.domain.Courier;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Courier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
    Optional<Courier> findByTelegramChatId(Long chatId);
}
