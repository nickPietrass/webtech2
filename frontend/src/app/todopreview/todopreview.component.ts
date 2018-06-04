import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-todopreview',
  templateUrl: './todopreview.component.html',
  styleUrls: ['./todopreview.component.css']
})
export class TodopreviewComponent implements OnInit {
  title = '';
  constructor() {
    this.title = 'testing title';
   }

  ngOnInit() {
  }

}
