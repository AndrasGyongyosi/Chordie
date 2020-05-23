import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Observable } from 'rxjs';
import { ChordComponent } from '../model/ChordComponent.model';
import httpConfig from './httpConfig.json';

@Injectable({
  providedIn: 'root'
})
export class ChordService {

  private controller = "chord";

  constructor(private http: HttpClient) { }

  getChordComponents(): Observable<ChordComponent> {
    return this.http.get<ChordComponent>(httpConfig.baseUrl + this.controller + "/components/");
  }

}
