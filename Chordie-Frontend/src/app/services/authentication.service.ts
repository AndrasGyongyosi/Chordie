import { Injectable, EventEmitter } from '@angular/core';
import { AuthService, GoogleLoginProvider } from 'angularx-social-login';
import { UserService } from './user.service';
import { InstrumentService } from './instrument.service';
import { ListService } from './list.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  isLoggedInEvent = new EventEmitter();

  constructor(private authService: AuthService, private userService: UserService, private instrumentService: InstrumentService, private listService: ListService) { }

  async login() {

    console.log("login")
    await this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
    this.authService.authState.subscribe((user) => {
      if (user) {
        localStorage.setItem("userFirstName", user.firstName);
        localStorage.setItem("userEmail", user.email);
        localStorage.setItem("userPhotoUrl", user.photoUrl);
        localStorage.setItem("userIdToken", user.idToken);
        this.userService.saveUser().subscribe(
          () => {
            this.instrumentService.getInstrumentsByUser().subscribe(
              (instruments) => this.instrumentService.instrumentsChanged.emit(instruments));

            this.listService.getLists().subscribe(
              (lists) => this.listService.listsChanged.emit(lists));
          });
        
        this.isLoggedInEvent.emit(localStorage.getItem("userIdToken"));
      }
    });   
    

  }

  async logout() {
    await this.authService.signOut(true);
    localStorage.clear();

    this.instrumentService.getInstrumentsByUser().subscribe(
      (instruments) => this.instrumentService.instrumentsChanged.emit(instruments));
      
    this.isLoggedInEvent.emit(localStorage.getItem("userIdToken"));
  }
}
