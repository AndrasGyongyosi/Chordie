import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CurrentUser } from '../model/CurrentUser.model';
import httpConfig from './httpConfig.json';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private controller = "user";

  constructor(private http: HttpClient) { }

  saveUser(): Observable<CurrentUser> {
    return this.http.post<CurrentUser>(httpConfig.baseUrl + this.controller + "/new",
     { token: localStorage.getItem("userIdToken"), email: localStorage.getItem("userEmail") });
  }
}
