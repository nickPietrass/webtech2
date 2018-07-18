import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-notify',
  templateUrl: './notify.component.html',
  styleUrls: ['./notify.component.css']
})
export class NotifyComponent implements OnInit {

  constructor() { }
  @Input() type;
  @Input() content;
  
  ngOnInit() {
  }

}
