import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-todopreview',
  templateUrl: './todopreview.component.html',
  styleUrls: ['./todopreview.component.css']
})
export class TodopreviewComponent implements OnInit {
  @Input() id : string;
  @Input() name : string;
  todo;
  viewMode: string;
  constructor() {
    this.viewMode = "minimized";
    //dummy data
    //TODO actually load data by id
    this.todo = { name: "testname", content: "Lorem Ipsum is simply dummy text of  asdasdasasd" };
  }

  ngOnInit() {
  }
  onEdit() {
    this.viewMode = "maximized";
    //TODO API Call
  }
}