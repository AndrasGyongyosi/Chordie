import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import httpConfig from '../configs/httpConfig.json';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private controller = "user";

  constructor(private http: HttpClient) { }

  protected httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  saveUser() {
    console.log(httpConfig.baseUrl + this.controller + "/new");
    console.log(localStorage.getItem("userIdToken"));
    return this.http.post(httpConfig.baseUrl + this.controller + "/new", {
      token: localStorage.getItem("userIdToken"), email: localStorage.getItem("userEmail")});
  }
}
