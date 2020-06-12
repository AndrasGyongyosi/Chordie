import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ScrollService {

  constructor() { }

  scrollToHome() {
    document.getElementById("home").scrollIntoView({behavior: "smooth", block: "start"}); 
  }

  scrollToAbout() {
    document.getElementById("about").scrollIntoView({behavior: "smooth", block: "start"});
  }

  scrollToIstrumentsAndLists() {
    document.getElementById("instrumentsandlists").scrollIntoView({behavior: "smooth", block: "start"});
  }

  scrollToChords() {
    document.getElementById("chords").scrollIntoView({behavior: "smooth", block: "start"});
  }

  scrollToListSelector() {
    document.getElementById("chordselector").scrollIntoView({behavior: "smooth", block: "start"});
  }
}
