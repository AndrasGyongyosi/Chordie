import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { CurrentUserService } from '../services/current-user.service';
import { AuthenticationService } from '../services/authentication.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  @Output() public isLoggedInEvent = new EventEmitter(); 
  currentUserName: string;
  currentUserEmail: string;
  currentUserPhotoUrl: string;

  constructor(private authenticationService: AuthenticationService, private currentUserService: CurrentUserService) { }

  ngOnInit() {

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

    this.currentUserName = this.currentUserService.currentUser.firstName;
    this.currentUserPhotoUrl = this.currentUserService.currentUser.photoUrl;
    this.currentUserEmail = this.currentUserService.currentUser.email;
    this.isLoggedInEvent.emit(this.currentUserName);
  }

  logout() {
    this.authenticationService.logout();

    this.currentUserName = null;
    this.isLoggedInEvent.emit(null);
  }


}
