import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import httpConfig from './httpConfig.json';
import { Instrument } from '../model/instrument.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InstrumentService {

  private controller = "instrument"

  constructor(private http: HttpClient) { }

  getInstrumentsByUser(): Observable<Instrument[]> {
    return this.http.get<Instrument[]>(httpConfig.baseUrl + this.controller + "/" + localStorage.getItem("userIdToken"));
  }
}
