import { Component, OnInit } from '@angular/core';
import { ChordService } from 'src/app/services/chord.service';
import { CatchResult } from 'src/app/models/catchResult.model';
import { Catch } from 'src/app/models/catch.model';
import { DialogService } from 'src/app/services/dialog-service';
import { ListService } from 'src/app/services/list.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { ScrollService } from 'src/app/services/scroll.service';
import { StoredCatch } from 'src/app/models/stored-catch.model';
import { List } from 'src/app/models/list.model';

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
  public selectedList: List;

  constructor(private chordService: ChordService, private dialogService: DialogService, private listService: ListService, private authService: AuthenticationService, public scrollService: ScrollService) { }

  ngOnInit(): void {
    this.isLoggedIn = localStorage.getItem("userIdToken");
    this.authService.isLoggedInEvent.subscribe(
      (isLoggedIn) => this.isLoggedIn = isLoggedIn)

    this.getSelectedList();

    this.listService.selectedListChanged.subscribe(
      (list) => this.selectedList = list)

    // Call if we requested for new chords by sending the parts of chord in path
    this.chordService.chordPathVariables.subscribe(
        (path) => {
          this.chordPathVariables = path

          if (this.chordPathVariables) {
            this.chordService.getChordCatches(this.chordPathVariables).subscribe(
              (chordCatches) => {
                console.log("getChordCatches: ")
                console.log(chordCatches)
                this.chordCatches = chordCatches
                this.bundsByCatch = this.chordService.calculateBundsForCatch(chordCatches)
                console.log(this.bundsByCatch)

                this.otherCatchIndexes = [];
                for (let j = 1; j < (this.bundsByCatch.length > 4 ? 4 : this.bundsByCatch.length); j++) {
                  this.otherCatchIndexes.push(j)
                }

                this.setHeight();
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

  playAudio(catch_: Catch, playTogether: boolean) {
    let audios = [];
    for (let string = 0; string < catch_.stringCatches.length; string++) {
      if (catch_.stringCatches[string].bund != -1) {
        let audio = new Audio();
        let sound = catch_.stringCatches[string].sound.label.split('#').join('s');
        audio.src = "assets/audio/guitar-acoustic/" + sound + (catch_.stringCatches[string].octave+3) + ".mp3";
        audios.push(audio);
      }
    }

    if (playTogether) {
      for (let i = 0; i < audios.length; i++) {
          audios[i].load();
          audios[i].play();
      }
    } else {
      for (let i = 0; i < audios.length; i++) {
        setTimeout(function() {
          audios[i].load();
          audios[i].play();
        }, 500 + i*500)
      } 
    }    
  }

  addCatchToList(catch_: Catch) {
    console.log(this.chordCatches.chord.rootNote)
    let storedCatch: StoredCatch = {};
    
    storedCatch.instrument = "Gitár";
    storedCatch.chord = this.chordCatches.chord;
    storedCatch.chord.rootNote = this.chordCatches.chord.rootNote;
    storedCatch.listToken = this.selectedList.listToken;
    storedCatch.stringCatches = catch_.stringCatches;
    console.log(storedCatch.stringCatches)
    console.log(catch_.stringCatches)

    this.listService.addToList(storedCatch).subscribe(
      (data) => {
        if (data) {
          console.log("catch added: "+ data)
        }
        this.listService.getLists().subscribe(
          (lists) => this.listService.listsChanged.emit(lists));
      });  
  }

  private setHeight() {
    if (this.chordCatches.catches.length > 0) {
      this.customMainHeight = this.chordCatches.catches[0].stringCatches.length * 40 + 'px';

      if (this.chordCatches.catches.length > 1) {
        this.customOtherHeight = this.chordCatches.catches[1].stringCatches.length * 35 + 'px';
      }

      let pageHeightWithoutAd = this.chordCatches.catches[0].stringCatches.length * 40 +
        ((this.chordCatches.catches.length > 1) ? this.chordCatches.catches[1].stringCatches.length : 0) * 35 + 615;
      this.pageHeight = pageHeightWithoutAd + 200 + 'px';
    }
  }

  private getSelectedList() {
    if (localStorage.getItem("listToken")) {
      this.listService.getListByToken(localStorage.getItem("listToken")).subscribe(
        (list) => this.selectedList = list)
    }
  }

}
