import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { ChordService } from 'src/app/services/chord.service';
import { CatchResult } from 'src/app/models/catchResult.model';
import { Catch } from 'src/app/models/catch.model';
import { DialogService } from 'src/app/services/dialog-service';
import { ListService } from 'src/app/services/list.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { ScrollService } from 'src/app/services/scroll.service';

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
  pageHeight;
  otherCatchIndexes = [];
  public isLoggedIn;

  constructor(private chordService: ChordService, private dialogService: DialogService, public listService: ListService, private authService: AuthenticationService, public scrollService: ScrollService) { }

  ngOnInit(): void {
    this.isLoggedIn = localStorage.getItem("userIdToken");
    this.bundsByCatch = [];

    this.authService.isLoggedInEvent.subscribe(
      (isLoggedIn) => this.isLoggedIn = isLoggedIn)

    this.chordService.chordPathVariables.subscribe(
        (path) => {
          this.chordPathVariables = path

          if (this.chordPathVariables) {
            this.chordService.getChordCatches(this.chordPathVariables).subscribe(
              (chordCatches) => {
                console.log("getChordCatches: ")
                console.log(chordCatches)
                this.chordCatches = chordCatches
                this.calculateBunds()
                console.log(this.chordCatches)
                this.customMainHeight = this.chordCatches.catches[0].stringCatches.length * 40 + 'px'
                if (this.chordCatches.catches.length > 1)
                  this.customOtherHeight = this.chordCatches.catches[1].stringCatches.length * 35 + 'px'

                let pageHeightWithoutAd = this.chordCatches.catches[0].stringCatches.length * 40 + 
                ((this.chordCatches.catches.length > 1) ? this.chordCatches.catches[1].stringCatches.length : 0) * 35 + 615
                this.pageHeight = pageHeightWithoutAd + 200 + 'px'
              }
          );
        }
      })

  }
  
  openCatchTipDialog() {
    this.dialogService.openCatchTipDialog().subscribe();
  }


  changeHelpOnHover(id) {
    (document.getElementById(id) as HTMLImageElement).src = "assets/img/help.png";    
  }

  changeHelpAfterHover(id) {
    (document.getElementById(id) as HTMLImageElement).src = "assets/img/help2.png";
  }

  playAudio(_catch: Catch){
    let audios = [];
    for (let string = 0; string < _catch.stringCatches.length; string++) {
      if (_catch.stringCatches[string].bund != -1) {
        let audio = new Audio();
        let sound = _catch.stringCatches[string].sound.split('#').join('s');
        audio.src = "assets/audio/guitar-acoustic/" + sound + "3.mp3";
        audios.push(audio);
      }
    }

    for (let i = 0; i < audios.length; i++) {
        setTimeout(function() {
          audios[i].load();
          audios[i].play();
        }, 500 + i*500)
      }  
  }

  playAudioChord(_catch: Catch){
    let audios = [];
    for (let string = 0; string < _catch.stringCatches.length; string++) {
      if (_catch.stringCatches[string].bund != -1) {
        let audio = new Audio();
        let sound = _catch.stringCatches[string].sound.split('#').join('s');
        audio.src = "assets/audio/guitar-acoustic/" + sound + "3.mp3";
        audios.push(audio);
      }
    }

    for (let i = 0; i < audios.length; i++) {
          audios[i].load();
          audios[i].play();
      }  
  }

  addCatchToList(catch_: Catch) {
    catch_.listToken = this.listService.selectedList.listToken;
    catch_.chord = this.chordCatches.chord;
    this.listService.addToList(catch_).subscribe(
      () => console.log(this.listService.selectedList)
    );

  }

  calculateBunds() {
    this.bundsByCatch = [[]];
    this.otherCatchIndexes = [];
    for (let j = 0; j < ((this.chordCatches.catches.length > 4) ? 4 : (this.chordCatches.catches.length)); j++) {
      let minBund = this.calculateMinBundByCatchAndCapo(this.chordCatches.catches[j], this.chordCatches.capo);
      this.bundsByCatch[j] = [minBund, minBund+1, minBund+2, minBund+3, minBund+4];
    }
    console.log(this.bundsByCatch)

    for (let j = 1; j < (this.bundsByCatch.length > 4 ? 4 : this.bundsByCatch.length); j++) {
      this.otherCatchIndexes.push(j)
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
