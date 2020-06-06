import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Observable, BehaviorSubject } from 'rxjs';
import { ChordComponent } from '../models/chordComponent.model';
import httpConfig from '../configs/httpConfig.json';
import { ChordModel } from '../models/chord.model';
import { CatchResult } from '../models/catchResult.model';

@Injectable({
  providedIn: 'root'
})
export class ChordService {

  private controller = "chord";

  private messageSource = new BehaviorSubject<string>("");
  chordPathVariables = this.messageSource.asObservable();

  constructor(private http: HttpClient) { }

  getChordComponents(): Observable<ChordComponent> {
    return this.http.get<ChordComponent>(httpConfig.baseUrl + this.controller + "/components/");
  }

  chordTextAnalyze(text): Observable<ChordModel> {
    return this.http.get<ChordModel>(httpConfig.baseUrl + this.controller + "/text/" + encodeURIComponent(text));
  }

  getChordCatches(pathVariables): Observable<CatchResult> {
    return this.http.get<CatchResult>(httpConfig.baseUrl + this.controller + "/catch/" + pathVariables);
  }

  changePath(path: string) {
    this.messageSource.next(path);
  }

}
