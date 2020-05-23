import { Injectable } from '@angular/core';
import { CurrentUser } from '../model/CurrentUser.model';

@Injectable({
  providedIn: 'root'
})
export abstract class CurrentUserService {

  abstract currentUser: CurrentUser;

  constructor() { }
}
