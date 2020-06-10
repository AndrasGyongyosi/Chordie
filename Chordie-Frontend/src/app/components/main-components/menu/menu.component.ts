import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { ScrollService } from 'src/app/services/scroll.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  currentUserName: string;
  currentUserEmail: string;
  currentUserPhotoUrl: string;

  constructor(private authenticationService: AuthenticationService, public scrollService: ScrollService) { }

  ngOnInit() {
    this.setCurrentUser();
    
    this.authenticationService.isLoggedInEvent.subscribe(
      () => this.setCurrentUser());  
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
    this.currentUserName = localStorage.getItem("userFirstName");
    this.currentUserPhotoUrl = localStorage.getItem("userPhotoUrl");
    this.currentUserEmail = localStorage.getItem("userEmail");
  }

}
