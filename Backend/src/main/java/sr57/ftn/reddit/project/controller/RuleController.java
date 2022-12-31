package sr57.ftn.reddit.project.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sr57.ftn.reddit.project.model.dto.ruleDTOs.AddRuleDTO;
import sr57.ftn.reddit.project.model.dto.ruleDTOs.RuleDTO;
import sr57.ftn.reddit.project.model.entity.Rule;
import sr57.ftn.reddit.project.service.RuleService;

import java.util.List;

@RestController
@RequestMapping("api/rules")
public class RuleController {
    final RuleService ruleService;
    final ModelMapper modelMapper;

    @Autowired
    public RuleController(RuleService ruleService, ModelMapper modelMapper) {
        this.ruleService = ruleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<RuleDTO>> getAll() {
        List<Rule> rules = ruleService.findAll();

        List<RuleDTO> rulesDTO = modelMapper.map(rules, new TypeToken<List<RuleDTO>>() {
        }.getType());
        return new ResponseEntity<>(rulesDTO, HttpStatus.OK);
    }

    @GetMapping("/{rule_id}")
    public ResponseEntity<RuleDTO> getRule(@PathVariable("rule_id") Integer rule_id) {
        Rule rule = ruleService.findOne(rule_id);
        return rule == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(modelMapper.map(rule, RuleDTO.class), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<AddRuleDTO> addRule(@RequestBody AddRuleDTO addRuleDTO) {
        Rule newRule = new Rule();

        newRule.setDescription(addRuleDTO.getDescription());

        newRule = ruleService.save(newRule);
        return new ResponseEntity<>(modelMapper.map(newRule, AddRuleDTO.class), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{rule_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRule(@PathVariable("rule_id") Integer rule_id) {
        Rule rule = ruleService.findOne(rule_id);

        if (rule != null) {
            ruleService.remove(rule_id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
