import { Component, Input, OnInit } from '@angular/core';
import { Rule } from 'src/app/model/rule.model';

@Component({
  selector: 'app-rule-item',
  templateUrl: './rule-item.component.html',
  styleUrls: ['./rule-item.component.css']
})
export class RuleItemComponent implements OnInit {

  @Input() rule: Rule = new Rule();
  constructor() { }

  ngOnInit() {
  }

}
