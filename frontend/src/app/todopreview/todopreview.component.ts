import { Component, OnInit, Input } from '@angular/core';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-todopreview',
  templateUrl: './todopreview.component.html',
  styleUrls: ['./todopreview.component.css']
})
export class TodopreviewComponent implements OnInit {
  @Input() id : string;
  viewMode: string;
  todo = {
    tudooUUID : "",
    title : "",
    content : ""
  };
  constructor(private api : ApiService) {
  }

  ngOnInit() {
    //initialize in minimized view
    this.viewMode = "minimized";
    //load the todo from cache
    this.todo = this.api.getTodoById(this.id);
  }
  onEdit(title, content) {
    this.viewMode = "maximized";
    //TODO API Call
    this.api.editTodo(this.todo.tudooUUID, title, content)
  }
}