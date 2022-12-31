import { Component, Input, OnInit } from '@angular/core';
import { Flair } from 'src/app/model/flair.model';

@Component({
  selector: 'app-flair-item',
  templateUrl: './flair-item.component.html',
  styleUrls: ['./flair-item.component.css']
})
export class FlairItemComponent implements OnInit {

  @Input() flair: Flair = new Flair();
  constructor() { }

  ngOnInit() {
  }

}
