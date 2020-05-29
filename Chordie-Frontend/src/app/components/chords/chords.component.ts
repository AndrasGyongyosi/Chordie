import { Component, OnInit, ViewChild } from '@angular/core';
import { ChordService } from 'src/app/services/chord.service';
import { CatchResult } from 'src/app/model/catchResult.model';

@Component({
  selector: 'app-chords',
  templateUrl: './chords.component.html',
  styleUrls: ['./chords.component.scss']
})
export class ChordsComponent implements OnInit {

 // public chordPathVariables = "BSXi3fllSARZ9JwTs7FpLEiIRolNwYZu/C/maj/maj7/undefined/0";
  public chordCatches: CatchResult;
  public chordPathVariables;
  public bundsByCatch: [number?, number?, number?, number?, number?][] = [[]];

  constructor(private chordService: ChordService) { }

  ngOnInit(): void {
    this.chordService.chordPathVariables.subscribe(
        (path) => {
          this.chordPathVariables = path

          if (this.chordPathVariables) {
            this.chordService.getChordCatches(this.chordPathVariables).subscribe(
              (chordCatches) => {
                this.chordCatches = chordCatches
                this.calculateBunds()
                console.log(this.chordCatches)
              }
          );
        }})
  }


  scrollToHome() {
    document.getElementById("home").scrollIntoView({behavior: "smooth", block: "start"}); 
  }


  calculateBunds() {
    for (let j = 0; j < 4; j++) {
      let minBund = this.calculateMinBundByCatch(this.chordCatches.catches[j]);
      this.bundsByCatch[j] = [minBund, minBund+1, minBund+2, minBund+3, minBund+4];
    }
  }

  private calculateMinBundByCatch(_catch): number {
    let minBund: number = 20;
    for (let string = 0; string < _catch.stringCatches.length; string++) {
      if (_catch.stringCatches[string].bund != -1 &&
          _catch.stringCatches[string].bund != 0  &&
          _catch.stringCatches[string].bund < minBund) { 
        minBund = _catch.stringCatches[string].bund;
      }
    }
    
    return minBund;
  }

}
