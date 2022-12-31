package sr57.ftn.reddit.project.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sr57.ftn.reddit.project.model.dto.commentDTOs.AddCommentDTO;
import sr57.ftn.reddit.project.model.dto.commentDTOs.CommentDTO;
import sr57.ftn.reddit.project.model.dto.postDTOs.PostDTO;
import sr57.ftn.reddit.project.model.dto.postDTOs.UpdatePostDTO;
import sr57.ftn.reddit.project.model.dto.reactionDTOs.ReactionForCommentAndPost;
import sr57.ftn.reddit.project.model.dto.reportDTOs.AddReportDTO;
import sr57.ftn.reddit.project.model.dto.reportDTOs.SimpleInfoReportDTO;
import sr57.ftn.reddit.project.model.dto.userDTOs.SimpleInfoUserDTO;
import sr57.ftn.reddit.project.model.entity.*;
import sr57.ftn.reddit.project.model.enums.ReactionType;
import sr57.ftn.reddit.project.model.enums.ReportStatus;
import sr57.ftn.reddit.project.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/posts")
public class PostController {
    final PostService postService;
    final ModelMapper modelMapper;
    final UserService userService;
    final CommunityService communityService;
    final CommentService commentService;
    final ReactionService reactionService;
    final ReportService reportService;

    @Autowired
    public PostController(PostService postService, UserService userService, CommunityService communityService, ModelMapper modelMapper, CommentService commentService, ReactionService reactionService, ReportService reportService) {
        this.postService = postService;
        this.userService = userService;
        this.communityService = communityService;
        this.modelMapper = modelMapper;
        this.commentService = commentService;
        this.reactionService = reactionService;
        this.reportService = reportService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<PostDTO>> getAll() {
        List<Post> posts = postService.findAllFromNonSuspendedCommunity();

        List<PostDTO> postsDTO = modelMapper.map(posts, new TypeToken<List<PostDTO>>() {
        }.getType());
        return new ResponseEntity<>(postsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{post_id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable("post_id") Integer post_id) {
        Post post = postService.findOne(post_id);
        return post == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(modelMapper.map(post, PostDTO.class), HttpStatus.OK);
    }

    @GetMapping(value = "/{post_id}/comments")
    public ResponseEntity<List<CommentDTO>> getPostComments(@PathVariable("post_id") Integer post_id) {
        try {
            Post post = postService.findOneWithComments(post_id);

            Set<Comment> comments = post.getComments();
            List<CommentDTO> commentsDTO = new ArrayList<>();
            for (Comment comment : comments) {
                CommentDTO commentDTO = new CommentDTO();

                commentDTO.setComment_id(comment.getComment_id());
                commentDTO.setText(comment.getText());
                commentDTO.setUser(modelMapper.map(comment.getUser(), SimpleInfoUserDTO.class));
                commentDTO.setReactions(modelMapper.map(comment.getReactions(), new TypeToken<Set<ReactionForCommentAndPost>>() {
                }.getType()));
                commentDTO.setReports(modelMapper.map(comment.getReports(), new TypeToken<Set<SimpleInfoReportDTO>>() {
                }.getType()));

                commentsDTO.add(commentDTO);
            }
            return new ResponseEntity<>(commentsDTO, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }

    //Not needed, can be solved on the Frontend

//    @GetMapping(value = "/{post_id}/karma")
//    public Integer calculateKarma(@PathVariable("post_id") Integer post_id) {
//        Post post = postService.findOne(post_id);
//        Set<Reaction> postReactions = post.getReactions();
//
//        Integer karma = 0;
//
//        for(Reaction reaction : postReactions) {
//            if(reaction.getReaction_type().equals(ReactionType.UPVOTE)) {
//                karma++;
//            }else if (reaction.getReaction_type().equals(ReactionType.DOWNVOTE)) {
//                karma--;
//            }
//        }
//        return karma;
//    }

    @PostMapping(value = "{post_id}/addComment")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<AddCommentDTO> addComment(@RequestBody AddCommentDTO addCommentDTO, @PathVariable("post_id") Integer post_id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Post post = postService.findOne(post_id);

        Comment newComment = new Comment();

        newComment.setText(addCommentDTO.getText());
        newComment.setTimestamp(LocalDate.now());
        newComment.setIs_deleted(false);
        newComment.setUser(user);
        newComment.setPost(post);

        newComment = commentService.save(newComment);

        Reaction newReaction = new Reaction();

        newReaction.setUser(user);
        newReaction.setTimestamp(LocalDate.now());
        newReaction.setComment(newComment);
        newReaction.setReaction_type(ReactionType.UPVOTE);
        newReaction.setPost(null);

        reactionService.save(newReaction);
        return new ResponseEntity<>(modelMapper.map(newComment, AddCommentDTO.class), HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/{post_id}/addReport")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<AddReportDTO> reportPost(@RequestBody AddReportDTO addReportDTO, @PathVariable("post_id") Integer post_id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Post post = postService.findOne(post_id);

        Report newReport = new Report();

        newReport.setReport_reason(addReportDTO.getReport_reason());
        newReport.setReport_status(ReportStatus.WAITING);
        newReport.setTimestamp(LocalDate.now());
        newReport.setAccepted(false);
        newReport.setComment(null);
        newReport.setPost(post);
        newReport.setUser(user);

        newReport = reportService.save(newReport);

        return new ResponseEntity<>(modelMapper.map(newReport, AddReportDTO.class), HttpStatus.CREATED);
    }

    @PutMapping(value = "updatePost/{post_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<UpdatePostDTO> updatePost(@RequestBody UpdatePostDTO updatePostDTO, @PathVariable("post_id") Integer post_id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Post post = postService.findOne(post_id);

        if (post == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (user.getUser_id() != post.getUser().getUser_id()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        post.setTitle(updatePostDTO.getTitle());
        post.setText(updatePostDTO.getText());

        post = postService.save(post);
        return new ResponseEntity<>(modelMapper.map(post, UpdatePostDTO.class), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{post_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<Void> deletePost(@PathVariable("post_id") Integer post_id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Post post = postService.findOne(post_id);

        if (user.getUser_id() != post.getUser().getUser_id()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        reactionService.deleteCommentReactionsByPostId(post_id);
        reactionService.deleteByPostId(post_id);
        commentService.deleteByPostId(post_id);
        reportService.deleteByPostId(post_id);
        postService.remove(post_id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}