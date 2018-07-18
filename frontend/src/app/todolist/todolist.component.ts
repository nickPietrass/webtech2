import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-todolist',
  templateUrl: './todolist.component.html',
  styleUrls: ['./todolist.component.css']
})
export class TodolistComponent implements OnInit {
  new = false;
  constructor(private api: ApiService) { }

  onNewSubmit(todo) {
    this.new = false;
    this.api.addTodo(todo, () => {
      this.api.loadAllTodos(() => { })
    });
  }
  ngOnInit() {
    this.api.loadAllTodos(() => { });
  }

}
