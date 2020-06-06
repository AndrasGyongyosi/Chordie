import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { ChordService } from 'src/app/services/chord.service';
import { CatchResult } from 'src/app/models/catchResult.model';
import { Catch } from 'src/app/models/catch.model';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DialogService } from 'src/app/services/dialog-service';

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
  customMainHeight;
  customOtherHeight;

  constructor(private chordService: ChordService, private dialogService: DialogService) { }

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
                this.customMainHeight = this.chordCatches.catches[0].stringCatches.length * 40 + 'px'
                this.customOtherHeight = this.chordCatches.catches[1].stringCatches.length * 35 + 'px'
              }
          );
        }})

  }

  openCatchTipDialog() {
    this.dialogService.openCatchTipDialog().subscribe();
  }

  scrollToHome() {
    document.getElementById("home").scrollIntoView({behavior: "smooth", block: "start"}); 
  }


  changeNoteOnHover(id) {
    (document.getElementById(id) as HTMLImageElement).src = "assets/img/note_white.png";
  }

  changeNoteAfterHover(id) {
    (document.getElementById(id) as HTMLImageElement).src = "assets/img/note.png";
  }

  changeAddOnHover(id) {
    (document.getElementById(id) as HTMLImageElement).src = "assets/img/add_white.png";
  }

  changeAddAfterHover(id) {
    (document.getElementById(id) as HTMLImageElement).src = "assets/img/add_black.png";
  }

  changeHelpOnHover(id) {
    (document.getElementById(id) as HTMLImageElement).src = "assets/img/help.png";
  }

  changeHelpAfterHover(id) {
    (document.getElementById(id) as HTMLImageElement).src = "assets/img/help2.png";
  }

  calculateBunds() {
    for (let j = 0; j < ((this.chordCatches.catches.length > 4) ? 4 : (this.chordCatches.catches.length)); j++) {
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
