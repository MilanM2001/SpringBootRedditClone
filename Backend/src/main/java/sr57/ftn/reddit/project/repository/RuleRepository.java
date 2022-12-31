package sr57.ftn.reddit.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sr57.ftn.reddit.project.model.entity.Rule;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Integer> {
}
