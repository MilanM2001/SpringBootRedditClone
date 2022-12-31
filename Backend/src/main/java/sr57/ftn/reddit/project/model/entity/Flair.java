package sr57.ftn.reddit.project.model.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flair")
public class Flair implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flair_id", unique = true, nullable = false)
    private Integer flair_id;

    @Column(name = "name", nullable = false)
    private String name;

    //Flair has many Communities
    @ManyToMany
    @JoinTable(name = "communityFlairs", joinColumns = @JoinColumn(name = "flair_id"), inverseJoinColumns = @JoinColumn(name = "community_id"))
    private Set<Community> communities = new HashSet<Community>();

    @OneToMany(mappedBy = "flair", fetch = FetchType.EAGER)
    private Set<Post> posts = new HashSet<Post>();
}
