package sr57.ftn.reddit.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sr57.ftn.reddit.project.model.entity.Community;
import sr57.ftn.reddit.project.model.entity.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Integer> {
    Optional<Community> findFirstByName(String name);

    @Query(nativeQuery = true, value = "Select * From community Left Join post On community.community_id = post.community_id Left Join report On report.post_id = post.post_id Where community.community_id = ?1 And community.is_suspended = False And report.accepted Is Null Or report.accepted is FALSE Order By rand()")
    Community findOneWithPosts(Integer community_id);

    @Query("select community from Community community join fetch community.rules rule where community.community_id = ?1")
    Community findOneWithRules(Integer community_id);

    @Query("select community from Community community join fetch community.flairs flair where community.community_id = ?1")
    Community findOneWithFlairs(Integer community_id);

    @Query(value = "select * from Community community where community.is_suspended = false", nativeQuery = true)
    List<Community> findAllNonSuspended();
}
