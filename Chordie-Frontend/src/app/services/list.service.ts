import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import httpConfig from '../configs/httpConfig.json';
import { List } from '../models/list.model';
import { Catch } from '../models/catch.model';
import { StoredCatch } from '../models/stored-catch.model';

@Injectable({
  providedIn: 'root'
})
export class ListService {

  private controller = 'list'

  public listsChanged = new EventEmitter();

  constructor(private http: HttpClient) { }

  addNewList(name: String): Observable<void> {
    return this.http.post<void>(httpConfig.baseUrl + this.controller + "/new", { name: name, listToken: '', userToken: localStorage.getItem("userIdToken"), catches: [] });
  }

  getLists(): Observable<List[]> {
    return this.http.get<List[]>(httpConfig.baseUrl + this.controller + "byuser/" + localStorage.getItem("userIdToken"));
  }

  getListByToken(listToken: string): Observable<List> {
    return this.http.get<List>(httpConfig.baseUrl + this.controller + "bytoken/" + listToken)
  }

  addToList(addedCatch: StoredCatch): Observable<boolean> {
    return this.http.post<boolean>(httpConfig.baseUrl + this.controller + "/addCatch", 
      {chord: {
        baseSound: addedCatch.chord.baseSound, 
        baseType: addedCatch.chord.baseType, 
        chordType: addedCatch.chord.chordType, 
        rootNote: addedCatch.chord.rootNote, 
        capo: addedCatch.chord.capo}, instrument: addedCatch.instrument});
  }

  deleteList(listToken: String): Observable<void> {
    return this.http.delete<void>(httpConfig.baseUrl + this.controller + "/delete/" + listToken);
  }

  deleteCatch(catchToken: String): Observable<void> {
    return this.http.delete<void>(httpConfig.baseUrl + this.controller + "/deleteCatch/" + catchToken);
  }
}
