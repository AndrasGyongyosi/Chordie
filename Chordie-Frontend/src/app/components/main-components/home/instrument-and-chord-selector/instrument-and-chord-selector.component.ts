import { Component, OnInit, ViewChild, ElementRef, AfterViewInit, ViewEncapsulation } from '@angular/core';
import { ChordService } from 'src/app/services/chord.service';
import { ChordComponent } from 'src/app/model/chordComponent.model';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Instrument } from 'src/app/model/instrument.model';
import { InstrumentService } from 'src/app/services/instrument.service';
import { ChordModel } from 'src/app/model/chord.model';

@Component({
  selector: 'app-instrument-and-chord-selector',
  templateUrl: './instrument-and-chord-selector.component.html',
  styleUrls: ['./instrument-and-chord-selector.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class InstrumentAndChordSelectorComponent implements OnInit, AfterViewInit {

  public instruments: Instrument[] = [];
  public chordComponent: ChordComponent;
  public capos = [0, 1, 2, 3, 4, 5]; 
  public selectedInstrument: Instrument;
  public selectedChordLabel: ChordModel = {};
  public selectedChordName: ChordModel = {};
  public chordText;

  public isLoggedIn;

  constructor(private chordService: ChordService, private authService: AuthenticationService, private instrumentService: InstrumentService) { }
  @ViewChild("typeChord") private _inputElement: ElementRef;

  ngOnInit(): void {
    this.chordService.getChordComponents().subscribe(
      data => this.chordComponent = data
    );

    this.instrumentService.getInstrumentsByUser().subscribe(
      instruments => {
        this.instruments = instruments
        this.selectedInstrument = this.instruments[0]
      }
    );

    this.selectedChordLabel.capo = 0;
    this.selectedChordName.capo = 0;

    this.isLoggedIn = localStorage.getItem("userIdToken");
    this.authService.isLoggedInEvent.subscribe(
      (isLoggedIn) => this.isLoggedIn = isLoggedIn
    );
  }

  ngAfterViewInit(): void {
    this._inputElement.nativeElement.focus();
  }

  selectInstrument(instrument) {
    this.selectedInstrument = instrument;
  }

  selectBaseSound(baseSoundLabel) {
    this.selectedChordLabel.baseSound = baseSoundLabel;
    this.selectedChordName.baseSound = this.chordComponent.baseSounds.find(c => c.label == baseSoundLabel).name
  }

  selectBaseType(baseTypeLabel) {
    this.selectedChordLabel.baseType = baseTypeLabel;
    this.selectedChordName.baseType = this.chordComponent.baseTypes.find(c => c.label == baseTypeLabel).name
  }

  selectChordType(chordTypeLabel) {
    this.selectedChordLabel.chordType = chordTypeLabel; 
    this.selectedChordName.chordType = this.chordComponent.chordTypes.find(c => c.label == chordTypeLabel).name
  }

  selectCapo(capoNumber) {
    this.selectedChordLabel.capo = capoNumber;
    this.selectedChordName.capo = capoNumber;
  }

  selectRootNote(rootNoteLabel) {
    this.selectedChordLabel.rootNote = rootNoteLabel;
    this.selectedChordName.rootNote = this.chordComponent.baseSounds.find(c => c.label == rootNoteLabel).name
  }

  fillInputWithExample() {
    this._inputElement.nativeElement.value = "Cmaj7/C.2"
    this.chordAnalyze("Cmaj7/C.2");
  }

  login() {
    this.authService.login();
  }

  chordAnalyze(text: String) {
    if (!text) {
      this.selectedChordLabel.baseSound = null;
      this.selectedChordLabel.baseType = null;
      this.selectedChordLabel.chordType = null;
      this.selectedChordLabel.capo = 0;
      this.selectedChordLabel.rootNote = null;
    } else {
      text = text.split('/').join('>');
      text = text.split('.').join('<');

      // Type example: Cmaj7/C.2
      this.chordService.chordTextAnalyze(text).subscribe(
        (data) => {
          this.selectedChordName = data;
          this.selectedChordLabel.baseSound = this.chordComponent.baseSounds.find(c => c.name == data.baseSound).label;
          this.selectedChordLabel.baseType = this.chordComponent.baseTypes.find(c => c.name == data.baseType).label;
          this.selectedChordLabel.chordType = this.chordComponent.chordTypes.find(c => c.name == data.chordType).label;
          this.selectedChordLabel.capo = this.selectedChordName.capo;
          this.selectedChordLabel.rootNote = data.rootNote ? this.chordComponent.baseSounds.find(c => c.name == data.rootNote).label : null;
        }
      );
     }
  }

  getChordCatches() {
    let rootNote = this.selectedChordName.rootNote !== undefined ? this.selectedChordName.rootNote : null;
    let pathVariables = this.selectedInstrument.token + "/" + this.selectedChordName.baseSound + "/" + this.selectedChordName.baseType + "/" + 
      this.selectedChordName.chordType + "/" + rootNote + "/" + this.selectedChordName.capo;
      console.log(pathVariables);
    this.chordService.changePath(pathVariables);
    document.getElementById("chords").scrollIntoView({behavior: "smooth", block: "start"});
  }

}
