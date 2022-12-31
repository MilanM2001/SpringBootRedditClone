import { Component, Input, OnInit } from '@angular/core';
import { Flair } from 'src/app/model/flair.model';
import { FlairService } from 'src/app/service/flair.service';

@Component({
  selector: 'app-flair-list',
  templateUrl: './flair-list.component.html',
  styleUrls: ['./flair-list.component.css']
})
export class FlairListComponent implements OnInit {

  @Input()
  flairs: Flair[] = [];

  constructor(private flairService: FlairService) { }

  ngOnInit(): void {

  }

}
