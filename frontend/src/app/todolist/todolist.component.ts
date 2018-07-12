import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-todolist',
  templateUrl: './todolist.component.html',
  styleUrls: ['./todolist.component.css']
})
export class TodolistComponent implements OnInit {
  todos = [];
  new = false;
  constructor() { }

  onNewSubmit(){
    //TODO reload list
    this.new = false;
  }
  ngOnInit() {
    //TODO actually load the list

    //test data
    
    this.todos.push({name: "testname", content: "testcontent", id : "124"});
    this.todos.push({name: "testname", content: "testcontent", id : "124"});
    this.todos.push({name: "testname", content: "testcontent", id : "124"});
    this.todos.push({name: "testname", content: "testcontent", id : "124"});
    this.todos.push({name: "testname", content: "testcontent", id : "124"});
    this.todos.push({name: "testname", content: "testcontent", id : "124"});
    
    this.todos.push({name: "testname", content: "testcontent", id : "124"});
    this.todos.push({name: "testname", content: "testcontent", id : "124"});
    this.todos.push({name: "testname", content: "testcontent", id : "124"});
    this.todos.push({name: "testname", content: "testcontent", id : "124"});
    this.todos.push({name: "testname", content: "testcontent", id : "124"});
    this.todos.push({name: "testname", content: "testcontent", id : "124"});

    this.todos.push({name: "testname", content: "testcontent", id : "124"});
    this.todos.push({name: "testname", content: "testcontent", id : "124"});
    this.todos.push({name: "testname", content: "testcontent", id : "124"});
    this.todos.push({name: "testname", content: "testcontent", id : "124"});
    this.todos.push({name: "testname", content: "testcontent", id : "124"});
    this.todos.push({name: "testname", content: "testcontent", id : "124"});
  }

}
