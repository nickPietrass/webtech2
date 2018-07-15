import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-todolist',
  templateUrl: './todolist.component.html',
  styleUrls: ['./todolist.component.css']
})
export class TodolistComponent implements OnInit {
  todos = [];
  new = false;
  constructor(private api : ApiService) { }

  onNewSubmit(todo){
    //TODO reload list
    this.new = false;

    //refresh todo list
    console.log(todo)
    this.api.addTodo(todo);
    this.todos = this.api.getCachedTodos();
  }
  ngOnInit() {
    //TODO actually load the list

    //load All Todos from API
    this.api.loadAllTodos();
    this.todos = this.api.getCachedTodos();
  }

}
