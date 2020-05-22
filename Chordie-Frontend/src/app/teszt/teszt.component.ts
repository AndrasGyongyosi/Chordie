import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-teszt',
  templateUrl: './teszt.component.html',
  styleUrls: ['./teszt.component.scss']
})
export class TesztComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  scroll() {
    document.getElementById("tesztid").scrollIntoView({behavior: "smooth", block: "start"});
  }

}
