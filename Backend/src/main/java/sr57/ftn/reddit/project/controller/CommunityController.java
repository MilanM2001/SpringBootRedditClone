package sr57.ftn.reddit.project.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sr57.ftn.reddit.project.model.dto.communityDTOs.*;
import sr57.ftn.reddit.project.model.dto.flairDTOs.FlairDTO;
import sr57.ftn.reddit.project.model.dto.postDTOs.AddPostDTO;
import sr57.ftn.reddit.project.model.dto.postDTOs.PostDTO;
import sr57.ftn.reddit.project.model.dto.reactionDTOs.ReactionDTO;
import sr57.ftn.reddit.project.model.dto.reportDTOs.SimpleInfoReportDTO;
import sr57.ftn.reddit.project.model.dto.ruleDTOs.RuleDTO;
import sr57.ftn.reddit.project.model.dto.userDTOs.UserDTO;
import sr57.ftn.reddit.project.model.entity.*;
import sr57.ftn.reddit.project.model.enums.ReactionType;
import sr57.ftn.reddit.project.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/communities")
public class CommunityController {
    final CommunityService communityService;
    final ModelMapper modelMapper;
    final UserService userService;
    final ModeratorService moderatorService;
    final RuleService ruleService;
    final FlairService flairService;
    final PostService postService;
    final ReactionService reactionService;

    @Autowired
    public CommunityController(CommunityService communityService, ModelMapper modelMapper, UserService userService, ModeratorService moderatorService, RuleService ruleService, FlairService flairService, PostService postService, ReactionService reactionService) {
        this.communityService = communityService;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.moderatorService = moderatorService;
        this.ruleService = ruleService;
        this.flairService = flairService;
        this.postService = postService;
        this.reactionService = reactionService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommunityDTO>> getAll() {
        List<Community> communities = communityService.findAllNonSuspended();

        List<CommunityDTO> communitiesDTO = modelMapper.map(communities, new TypeToken<List<CommunityDTO>>() {
        }.getType());
        return new ResponseEntity<>(communitiesDTO, HttpStatus.OK);
    }

    @GetMapping("/{community_id}")
    public ResponseEntity<CommunityDTO> getCommunity(@PathVariable("community_id") Integer community_id) {
        Community community = communityService.findOne(community_id);
        return community == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(modelMapper.map(community, CommunityDTO.class), HttpStatus.OK);
    }

    @GetMapping(value = "/{community_id}/posts")
    public ResponseEntity<List<PostDTO>> getCommunityPosts(@PathVariable Integer community_id) {
        try {
            Community community = communityService.findOneWithPosts(community_id);

            Set<Post> posts = community.getPosts();
            List<PostDTO> postsDTO = new ArrayList<>();
            for (Post post : posts) {
                PostDTO postDTO = new PostDTO();

                postDTO.setPost_id(post.getPost_id());
                postDTO.setTitle(post.getTitle());
                postDTO.setText(post.getText());
                postDTO.setImage_path(post.getImage_path());
                postDTO.setCommunity(modelMapper.map(post.getCommunity(), CommunityDTO.class));
                postDTO.setUser(modelMapper.map(post.getUser(), UserDTO.class));
                postDTO.setReactions(modelMapper.map(post.getReactions(), new TypeToken<Set<ReactionDTO>>() {
                }.getType()));
                postDTO.setReports(modelMapper.map(post.getReports(), new TypeToken<Set<SimpleInfoReportDTO>>() {
                }.getType()));

                postsDTO.add(postDTO);
            }
            return new ResponseEntity<>(postsDTO, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{community_id}/rules")
    public ResponseEntity<List<RuleDTO>> getCommunityRules(@PathVariable Integer community_id) {
        try {
            Community community = communityService.findOneWithRules(community_id);

            Set<Rule> rules = community.getRules();
            List<RuleDTO> rulesDTO = new ArrayList<>();
            for (Rule rule : rules) {
                RuleDTO ruleDTO = new RuleDTO();

                ruleDTO.setRule_id(rule.getRule_id());
                ruleDTO.setDescription(rule.getDescription());

                rulesDTO.add(ruleDTO);
            }
            return new ResponseEntity<>(rulesDTO, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{community_id}/flairs")
    public ResponseEntity<List<FlairDTO>> getCommunityFlairs(@PathVariable Integer community_id) {
        try {
            Community community = communityService.findOneWithFlairs(community_id);

            Set<Flair> flairs = community.getFlairs();
            List<FlairDTO> flairsDTO = new ArrayList<>();
            for (Flair flair : flairs) {
                FlairDTO flairDTO = new FlairDTO();

                flairDTO.setFlair_id(flair.getFlair_id());
                flairDTO.setName(flair.getName());

                flairsDTO.add(flairDTO);
            }
            return new ResponseEntity<>(flairsDTO, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<AddCommunityDTO> addCommunity(@RequestBody AddCommunityDTO addCommunityDTO, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Optional<Community> name = communityService.findFirstByName(addCommunityDTO.getName());

        if (name.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        Community newCommunity = modelMapper.map(addCommunityDTO, Community.class);

        newCommunity.setName(addCommunityDTO.getName());
        newCommunity.setDescription(addCommunityDTO.getDescription());
        newCommunity.setCreation_date(LocalDate.now());
        newCommunity.setIs_suspended(false);
        newCommunity.setSuspended_reason("Not Suspended");

        Community savedCommunity = communityService.save(newCommunity);

        Set<Flair> flairs = addCommunityDTO.getFlairs().stream().map(flair -> {
            Flair f = new Flair();
            f.getCommunities().add(savedCommunity);
            f.setName(flair.getName());
            return this.flairService.save(f);
        }).collect(Collectors.toSet());

        savedCommunity.setFlairs(flairs);

        for (RuleDTO ruleDTO : addCommunityDTO.getRules()) {
            Rule newRule = new Rule();
            newRule.setDescription(ruleDTO.getDescription());
            newRule.setCommunity(newCommunity);
            ruleService.save(newRule);
        }

        Moderator newModerator = new Moderator();
        newModerator.setUser(user);
        newModerator.setCommunity(newCommunity);

        moderatorService.save(newModerator);
        return new ResponseEntity<>(modelMapper.map(newCommunity, AddCommunityDTO.class), HttpStatus.CREATED);
    }

    @PostMapping(value = "{community_id}/addPost")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<AddPostDTO> addPost(@RequestBody AddPostDTO addPostDTO, @PathVariable("community_id") Integer community_id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Community community = communityService.findOne(community_id);

        Post newPost = new Post();

        newPost.setTitle(addPostDTO.getTitle());
        newPost.setText(addPostDTO.getText());
        newPost.setCreation_date(LocalDate.now());
        newPost.setImage_path("https://upload.wikimedia.org/wikipedia/commons/1/1e/Battle_of_Kosovo%2C_Adam_Stefanovi%C4%87%2C_1870.jpg");
        newPost.setUser(user);
        newPost.setCommunity(community);
        newPost.setFlair(null);

        newPost = postService.save(newPost);

        Reaction newReaction = new Reaction();

        newReaction.setUser(user);
        newReaction.setTimestamp(LocalDate.now());
        newReaction.setComment(null);
        newReaction.setReaction_type(ReactionType.UPVOTE);
        newReaction.setPost(newPost);

        reactionService.save(newReaction);

        return new ResponseEntity<>(modelMapper.map(newPost, AddPostDTO.class), HttpStatus.CREATED);
    }

    @PutMapping(value = "/updateCommunity/{community_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<UpdateCommunityDTO> updateCommunity(@RequestBody UpdateCommunityDTO updateCommunityDTO, @PathVariable("community_id") Integer community_id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Community community = communityService.findOne(community_id);

        if (community == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        community.setDescription(updateCommunityDTO.getDescription());

        community = communityService.save(community);

        return new ResponseEntity<>(modelMapper.map(community, UpdateCommunityDTO.class), HttpStatus.OK);
    }

    //Only Admin can suspend a community
    @PutMapping(value = "/suspendCommunity/{community_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin
    public ResponseEntity<SuspendCommunityDTO> suspendCommunity(@RequestBody SuspendCommunityDTO suspendCommunityDTO, @PathVariable("community_id") Integer community_id) {
        Community community = communityService.findOne(community_id);

        if (community == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        community.setSuspended_reason(suspendCommunityDTO.getSuspended_reason());
        community.setIs_suspended(true);
//        community.setModerators(null);
        moderatorService.deleteByCommunityId(community_id);

        community = communityService.save(community);
        return new ResponseEntity<>(modelMapper.map(community, SuspendCommunityDTO.class), HttpStatus.OK);
    }

    //Not used
    @DeleteMapping(value = "/{community_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCommunity(@PathVariable("community_id") Integer community_id) {
        Community community = communityService.findOne(community_id);

        if (community != null) {
            communityService.remove(community_id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //Android

    @PostMapping(value = "/addCommunityAndroid")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<AddCommunityDTOAndroid> addCommunityAndroid(@RequestBody AddCommunityDTOAndroid addCommunityDTOAndroid, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Optional<Community> name = communityService.findFirstByName(addCommunityDTOAndroid.getName());

        if (name.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        Community newCommunity = modelMapper.map(addCommunityDTOAndroid, Community.class);

        newCommunity.setName(addCommunityDTOAndroid.getName());
        newCommunity.setDescription(addCommunityDTOAndroid.getDescription());
        newCommunity.setCreation_date(LocalDate.now());
        newCommunity.setIs_suspended(false);
        newCommunity.setSuspended_reason("Not Suspended");

        Community savedCommunity = communityService.save(newCommunity);

        Moderator newModerator = new Moderator();
        newModerator.setUser(user);
        newModerator.setCommunity(newCommunity);

        moderatorService.save(newModerator);
        return new ResponseEntity<>(modelMapper.map(newCommunity, AddCommunityDTOAndroid.class), HttpStatus.CREATED);
    }

}
