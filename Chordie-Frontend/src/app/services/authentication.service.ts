import { Injectable } from '@angular/core';
import { CurrentUserService } from './current-user.service';
import { AuthService, GoogleLoginProvider } from 'angularx-social-login';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private authService: AuthService, private currentUserService: CurrentUserService) { }

  async login() {
    await this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);

    this.authService.authState.subscribe((user) => {
      this.currentUserService.currentUser = user;
    });    
  }

  logout() {
    this.authService.signOut();
    this.currentUserService.currentUser = null;
  }
}
