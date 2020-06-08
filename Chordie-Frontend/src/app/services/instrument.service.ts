import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import httpConfig from '../configs/httpConfig.json';
import { Instrument } from '../models/instrument.model';
import { Observable } from 'rxjs';
import { ChordProperty } from '../models/chordProperty.model';

@Injectable({
  providedIn: 'root'
})
export class InstrumentService {

  private controller = "instrument"

  public instrumentsChanged = new EventEmitter();

  constructor(private http: HttpClient) { }

  getInstrumentsByUser(): Observable<Instrument[]> {
    return this.http.get<Instrument[]>(httpConfig.baseUrl + this.controller + "/" + localStorage.getItem("userIdToken"));
  }

  addNewInstrumentForUser(instrumentName: string, maxBundDif: number, bundNumber: number, strings: ChordProperty[]): Observable<boolean> {
    console.log(instrumentName, maxBundDif, bundNumber, strings);
    return this.http.post<boolean>(httpConfig.baseUrl + this.controller + "/new", 
        { userToken: localStorage.getItem("userIdToken"), name: instrumentName, maxBundDif: maxBundDif, bundNumber: bundNumber, strings: strings});  
  }

  editInstrumentByUser(instrumentToken: string, instrumentName: string, maxBundDif: number, bundNumber: number, strings: ChordProperty[]): Observable<boolean> {
    console.log(instrumentToken, instrumentName, maxBundDif, bundNumber, strings);
    return this.http.post<boolean>(httpConfig.baseUrl + this.controller + "/edit/" + instrumentToken, 
        { name: instrumentName, maxBundDif: maxBundDif, bundNumber: bundNumber, strings: strings});
  }

  deleteInstrument(instrumentToken): Observable<boolean> {
    return this.http.delete<boolean>(httpConfig.baseUrl + this.controller + "/delete/" + instrumentToken);
  }
}
