import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  currentUserName: string;
  currentUserEmail: string;
  currentUserPhotoUrl: string;

  constructor(private authenticationService: AuthenticationService) { }

  ngOnInit() {
    this.setCurrentUser();
    
    this.authenticationService.isLoggedInEvent.subscribe(
      () => {
        this.setCurrentUser();
      });  
  }  

  scrollToAbout() {
    document.getElementById("about").scrollIntoView({behavior: "smooth", block: "start"});
  }

  scrollToHome() {
    document.getElementById("home").scrollIntoView({behavior: "smooth", block: "start"});
  }

  scrollToIstrumentsAndLists() {
    document.getElementById("instrumentsandlists").scrollIntoView({behavior: "smooth", block: "start"});
  }

  async login() {
    await this.authenticationService.login();
    
    this.setCurrentUser();
  }

  logout() {
    this.authenticationService.logout();
    this.currentUserName = null;
  }

  private setCurrentUser() {
    this.currentUserName = localStorage.getItem("userFirstName")
    this.currentUserPhotoUrl = localStorage.getItem("userPhotoUrl");
    this.currentUserEmail = localStorage.getItem("userEmail");
  }


}
