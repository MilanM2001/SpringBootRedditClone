package sr57.ftn.reddit.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sr57.ftn.reddit.project.model.entity.Flair;

import java.util.Optional;

@Repository
public interface FlairRepository extends JpaRepository<Flair, Integer> {
    Optional<Flair> findFirstByName(String name);
}
