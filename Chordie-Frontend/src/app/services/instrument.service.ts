import { Injectable } from '@angular/core';
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

  constructor(private http: HttpClient) { }

  getInstrumentsByUser(): Observable<Instrument[]> {
    return this.http.get<Instrument[]>(httpConfig.baseUrl + this.controller + "/" + localStorage.getItem("userIdToken"));
  }

  addNewInstrumentForUser(instrumentName: string, maxBundDif: string, bundNumber: string, strings: ChordProperty[]): Observable<boolean> {
    return this.http.post<boolean>(httpConfig.baseUrl + this.controller + "/new", 
        { user: localStorage.getItem("userIdToken"), instrumentalName: instrumentName, maxBundDif: maxBundDif, bundNumber: bundNumber, strings: strings});  
  }

  editInstrumentByUser(instrumentToken: string, instrumentName: string, maxBundDif: string, bundNumber: string, strings: ChordProperty[]): Observable<String> {
    console.log(instrumentToken, instrumentName, maxBundDif, bundNumber, strings);
    return this.http.post<String>(httpConfig.baseUrl + this.controller + "/edit/" + instrumentToken, 
        { instrumentalName: instrumentName, maxBundDif: maxBundDif, bundNumber: bundNumber, strings: strings});
  }

  deleteInstrument(instrumentToken): Observable<String> {
    return this.http.delete<String>(httpConfig.baseUrl + this.controller + "/delete/" + instrumentToken);

  }
}
