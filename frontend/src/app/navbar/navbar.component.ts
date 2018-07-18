import { Component, OnInit, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  @Output() pageChange = new EventEmitter<string>();
  pages: Array<Object> = [];

  constructor() { }


  ngOnInit() {

    // test data


    this.pages.push({ name: 'Login', newPage: 'login' });
    this.pages.push({ name: 'Profile', newPage: 'profile' });
    this.pages.push({ name: 'TuDoos', newPage: 'todos' });
    //this.pages.push({ name: 'Groups', newPage: 'groups' });
    this.pages.push({ name: 'Credits', newPage: 'credits' });
  }

  onPageClick(newPage: string) {
    this.pageChange.emit(newPage);
  }
}