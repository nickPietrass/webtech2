import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-todonew',
  templateUrl: './todonew.component.html',
  styleUrls: ['./todonew.component.css']
})
export class TodonewComponent implements OnInit {
  //TODO actual return type
  @Output() submit = new EventEmitter<string>();
  constructor() { }

  ngOnInit() {
  }

  submitNew(){
    //TODO API call
    this.submit.emit("");
  }
}
