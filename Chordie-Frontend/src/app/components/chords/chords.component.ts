import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { ChordService } from 'src/app/services/chord.service';
import { CatchResult } from 'src/app/model/catchResult.model';
import { Catch } from 'src/app/model/catch.model';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CommonDialogService } from 'src/app/services/common-dialog-service';

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
  panelOpenState;

  constructor(private chordService: ChordService, private commonDialogService: CommonDialogService) { }

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

  openCatchTipDialog() {
    this.commonDialogService.openCatchTipDialog().subscribe();
  }

  scrollToHome() {
    document.getElementById("home").scrollIntoView({behavior: "smooth", block: "start"}); 
  }

  calculateBunds() {
    for (let j = 0; j < 4; j++) {
      let minBund = this.calculateMinBundByCatchAndCapo(this.chordCatches.catches[j], this.chordCatches.capo);
      this.bundsByCatch[j] = [minBund, minBund+1, minBund+2, minBund+3, minBund+4];
    }
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

@Component({
  selector: 'catch-tip-dialog.component',
  templateUrl: 'catch-tip-dialog.component.html',
  styleUrls: ['./catch-tip-dialog.component.scss']
})

export class CatchTipDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<CatchTipDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {}

  ngOnInit(): void {
    
  }

  onNoClick(): void {
    console.log("close")
    this.dialogRef.close();
  }

}
