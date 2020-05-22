import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  scrollToAbout() {
    document.getElementById("about").scrollIntoView({behavior: "smooth", block: "start"});
  }

  scrollToHome() {
    document.getElementById("home").scrollIntoView({behavior: "smooth", block: "start"});
  }

}
