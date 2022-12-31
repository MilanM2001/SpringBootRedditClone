import { Component, Input, OnInit } from '@angular/core';
import { Rule } from 'src/app/model/rule.model';
import { RuleService } from 'src/app/service/rule.service';

@Component({
  selector: 'app-rule-list',
  templateUrl: './rule-list.component.html',
  styleUrls: ['./rule-list.component.css']
})
export class RuleListComponent implements OnInit {

  @Input()
  rules: Rule[] = [];

  constructor(private ruleService: RuleService) { }

  ngOnInit(): void {
    // this.ruleService.getAll().subscribe(data => {
    //   this.rules = data as Rule[];
    //   console.log(data);
    // })
  }

}
