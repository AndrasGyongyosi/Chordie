import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Observable, BehaviorSubject } from 'rxjs';
import { ChordComponent } from '../models/chordComponent.model';
import httpConfig from '../configs/httpConfig.json';
import { Chord } from '../models/chord.model';
import { CatchResult } from '../models/catchResult.model';
import { ChordProperty } from '../models/chordProperty.model';
import { Catch } from '../models/catch.model';
import { StoredCatch } from '../models/stored-catch.model';

@Injectable({
  providedIn: 'root'
})
export class ChordService {

  private controller = "chord";

  private messageSource = new BehaviorSubject<string>("");
  chordPathVariables = this.messageSource.asObservable();

  constructor(private http: HttpClient) { }

  changePath(path: string) {
    this.messageSource.next(path);
  }

  getChordComponents(): Observable<ChordComponent> {
    return this.http.get<ChordComponent>(httpConfig.baseUrl + this.controller + "/components/");
  }

  chordTextAnalyze(text): Observable<Chord> {
    return this.http.get<Chord>(httpConfig.baseUrl + this.controller + "/text/" + encodeURIComponent(text));
  }

  getChordCatches(pathVariables): Observable<CatchResult> {
    return this.http.get<CatchResult>(httpConfig.baseUrl + this.controller + "/catch/" + pathVariables);
  }

  getSoundsByChordComponents(baseSound, baseType, chordType): Observable<ChordProperty[]> {
    return this.http.get<ChordProperty[]>(httpConfig.baseUrl + this.controller + "/sound/" + baseSound + "/" + baseType + "/" + chordType);
  }







  calculateBunds(chordCatches) : any {
    let bundsByCatch = [[]];
    for (let j = 0; j < ((chordCatches.catches.length > 4) ? 4 : (chordCatches.catches.length)); j++) {
        let minBund = this.calculateMinBundByCatchAndCapo(chordCatches.catches[j], chordCatches.chord.capo);
        bundsByCatch[j] = [minBund, minBund+1, minBund+2, minBund+3, minBund+4];
    }
    return bundsByCatch;
    
  }

  calculateBunds2(chordCatches: StoredCatch[]) : any {
    let bundsByCatch = [[]];
    for (let j = 0; j < chordCatches.length; j++) {
        let minBund = this.calculateMinBundByCatchAndCapo(chordCatches[j], chordCatches[j].chord.capo);
        bundsByCatch[j] = [minBund, minBund+1, minBund+2, minBund+3, minBund+4];
    }
    return bundsByCatch;
    
  }

  private calculateMinBundByCatchAndCapo(_catch: Catch, capo: number): number {
    if (this.checkShowCapo(_catch, capo)) {
        return capo;
    }

    let minBund: number = 20;
    for (let stringIndex = 0; stringIndex < _catch.stringCatches.length; stringIndex++) {
      if (_catch.stringCatches[stringIndex].bund != -1 &&
        _catch.stringCatches[stringIndex].bund != 0 &&
        _catch.stringCatches[stringIndex].bund < minBund) {
        minBund = _catch.stringCatches[stringIndex].bund;
      }
    }

    return minBund;
  }

  private checkShowCapo(_catch: Catch, capo: number): boolean {
    if (capo != 0 ) {
      let showCapo: boolean = true;
      for (let stringIndex = 0; stringIndex < _catch.stringCatches.length; stringIndex++) {
        if (_catch.stringCatches[stringIndex].bund - capo >= 5) {
          return showCapo = false;
        }
      }

      return true;
    }

    return false;
  }

}
