package sr57.ftn.reddit.project.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sr57.ftn.reddit.project.model.dto.commentDTOs.CommentDTO;
import sr57.ftn.reddit.project.model.dto.commentDTOs.UpdateCommentDTO;
import sr57.ftn.reddit.project.model.dto.reportDTOs.AddReportDTO;
import sr57.ftn.reddit.project.model.entity.Comment;
import sr57.ftn.reddit.project.model.entity.Report;
import sr57.ftn.reddit.project.model.entity.User;
import sr57.ftn.reddit.project.model.enums.ReportStatus;
import sr57.ftn.reddit.project.service.CommentService;
import sr57.ftn.reddit.project.service.ReactionService;
import sr57.ftn.reddit.project.service.ReportService;
import sr57.ftn.reddit.project.service.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/comments")
public class CommentController {
    final CommentService commentService;
    final ModelMapper modelMapper;
    final UserService userService;
    final ReactionService reactionService;
    final ReportService reportService;

    @Autowired
    public CommentController(CommentService commentService, ModelMapper modelMapper, UserService userService, ReactionService reactionService, ReportService reportService) {
        this.commentService = commentService;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.reactionService = reactionService;
        this.reportService = reportService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<CommentDTO>> GetAll() {
        List<Comment> comments = commentService.findAll();

        List<CommentDTO> commentsDTO = modelMapper.map(comments, new TypeToken<List<CommentDTO>>() {
        }.getType());
        return new ResponseEntity<>(commentsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/single/{comment_id}")
    public ResponseEntity<CommentDTO> GetComment(@PathVariable("comment_id") Integer comment_id) {
        Comment comment = commentService.findOne(comment_id);
        return comment == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(modelMapper.map(comment, CommentDTO.class), HttpStatus.OK);
    }

    @PostMapping(value = "/addReport/{comment_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<AddReportDTO> ReportComment(@RequestBody AddReportDTO addReportDTO, @PathVariable("comment_id") Integer comment_id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Comment comment = commentService.findOne(comment_id);

        Report newReport = new Report();

        newReport.setReport_reason(addReportDTO.getReport_reason());
        newReport.setReport_status(ReportStatus.WAITING);
        newReport.setTimestamp(LocalDate.now());
        newReport.setAccepted(false);
        newReport.setComment(comment);
        newReport.setPost(null);
        newReport.setUser(user);

        newReport = reportService.save(newReport);

        return new ResponseEntity<>(modelMapper.map(newReport, AddReportDTO.class), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{comment_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<UpdateCommentDTO> UpdateComment(@RequestBody UpdateCommentDTO updateCommentDTO, @PathVariable("comment_id") Integer comment_id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Comment comment = commentService.findOne(comment_id);

        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (user.getUser_id() != comment.getUser().getUser_id()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        comment.setText(updateCommentDTO.getText());

        comment = commentService.save(comment);
        return new ResponseEntity<>(modelMapper.map(comment, UpdateCommentDTO.class), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{comment_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<Void> DeleteComment(@PathVariable("comment_id") Integer comment_id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Comment comment = commentService.findOne(comment_id);

        if (user.getUser_id() != comment.getUser().getUser_id()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        reactionService.deleteByCommentId(comment_id);
        reportService.deleteByCommentId(comment_id);
        commentService.remove(comment_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
