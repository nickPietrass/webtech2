import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-todonew',
  templateUrl: './todonew.component.html',
  styleUrls: ['./todonew.component.css']
})
export class TodonewComponent implements OnInit {
  @Output() submit = new EventEmitter<object>();

  todoName = "";
  todoContent = "";

  constructor(private api : ApiService) { }

  ngOnInit() {
  }

  submitNew(name, content){
    //TODO API call
    console.log(this.todoName);
    console.log(this.todoContent);
    let newTodo = {
      id: this.api.getNewId(),
      name: name,
      content: content,
      editableBy: ["dude", "dude2"]
    }
    this.submit.emit(newTodo);
  }
}
