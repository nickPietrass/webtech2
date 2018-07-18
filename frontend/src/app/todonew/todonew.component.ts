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
    console.log(name);
    console.log(content);
    let newTodo = {
      title: name,
      content: content
    }
    this.submit.emit(newTodo);
  }
}
