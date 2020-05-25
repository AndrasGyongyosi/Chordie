import { Injectable, EventEmitter } from '@angular/core';
import { AuthService, GoogleLoginProvider } from 'angularx-social-login';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  isLoggedInEvent = new EventEmitter();

  constructor(private authService: AuthService, private userService: UserService) { }

  async login() {
    await this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
    
    this.authService.authState.subscribe((user) => {
      localStorage.setItem("userFirstName", user.firstName);
      localStorage.setItem("userEmail", user.email);
      localStorage.setItem("userPhotoUrl", user.photoUrl);
      localStorage.setItem("userIdToken", user.idToken);
    });   
    
    this.isLoggedInEvent.emit(localStorage.getItem("userIdToken"));
    
    this.userService.saveUser();
  }

  logout() {
    this.authService.signOut();
    localStorage.clear();
    this.isLoggedInEvent.emit(localStorage.getItem("userIdToken"));
  }
}
