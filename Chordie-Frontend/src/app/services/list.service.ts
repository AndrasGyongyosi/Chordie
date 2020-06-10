import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import httpConfig from '../configs/httpConfig.json';
import { List } from '../models/list.model';
import { Catch } from '../models/catch.model';

@Injectable({
  providedIn: 'root'
})
export class ListService {

  private controller = 'list'

  public listsChanged = new EventEmitter();
  public selectedList: List;

  constructor(private http: HttpClient) { }

  addNewList(name: String): Observable<void> {
    return this.http.post<void>(httpConfig.baseUrl + this.controller + "/new", { name: name, listToken: '', userToken: localStorage.getItem("userIdToken"), catches: [] });
  }

  getLists(): Observable<List[]> {
    console.log(httpConfig.baseUrl + this.controller + "/" + localStorage.getItem("userIdToken"))
    return this.http.get<List[]>(httpConfig.baseUrl + this.controller + "/" + localStorage.getItem("userIdToken"));
  }

  addToList(addedCatch: Catch): Observable<boolean> {
    return this.http.post<boolean>(httpConfig.baseUrl + this.controller + "/addCatch", 
      {chord: addedCatch.chord, listToken: addedCatch.listToken, stringCatches: addedCatch.stringCatches, instrument: addedCatch.instrument});
  }

  deleteList(listToken: String): Observable<void> {
    return this.http.delete<void>(httpConfig.baseUrl + this.controller + "/delete/" + listToken);
  }

  deleteCatch(catchToken: String): Observable<void> {
    return this.http.delete<void>(httpConfig.baseUrl + this.controller + "/deleteCatch/" + catchToken);
  }
}
