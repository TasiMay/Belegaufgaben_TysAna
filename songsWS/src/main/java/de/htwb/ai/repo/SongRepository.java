package de.htwb.ai.repo;

import de.htwb.ai.model.Song;

import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    @Transactional
    Optional<Song> findById(int id);

    @Transactional
    void deleteById(int id);

    @Transactional
    boolean existsById(int id);

}