import { Component, OnInit , EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {
  @Output() loggedIn = new EventEmitter<boolean>();
  constructor() { }

  ngOnInit() {
  }
  login(){
    this.loggedIn.emit(true);
  }
}
