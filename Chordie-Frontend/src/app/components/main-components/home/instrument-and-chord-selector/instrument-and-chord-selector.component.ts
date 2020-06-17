import { Component, OnInit, ViewChild, ElementRef, AfterViewInit, ViewEncapsulation } from '@angular/core';
import { ChordService } from 'src/app/services/chord.service';
import { ChordComponent } from 'src/app/models/chordComponent.model';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Instrument } from 'src/app/models/instrument.model';
import { InstrumentService } from 'src/app/services/instrument.service';
import { Chord } from 'src/app/models/chord.model';
import { ChordProperty } from 'src/app/models/chordProperty.model';
import { ListService } from 'src/app/services/list.service';
import { List } from 'src/app/models/list.model';
import { ScrollService } from 'src/app/services/scroll.service';

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
  public selectedChord: Chord = {};
  public chordText;
  public randomExample;
  public rootNotes: ChordProperty[] = [];
  public lists: List[];

  public isLoggedIn;

  constructor(private chordService: ChordService, private authService: AuthenticationService, private instrumentService: InstrumentService, private listService: ListService, private scrollService: ScrollService) { }
  @ViewChild("typeChord") private _inputElement: ElementRef;

  ngOnInit(): void {
    this.chordService.getChordComponents().subscribe(
      data => {
        this.chordComponent = data
        console.log(data)
        this.randomExample = this.generateRandomExample()
      }
    );

    this.instrumentService.getInstrumentsByUser().subscribe(
      instruments => {
        this.instruments = instruments
        this.selectedInstrument = this.instruments[0]
      }
    );

    this.selectedChord.capo = 0;

    this.isLoggedIn = localStorage.getItem("userIdToken");
    this.authService.isLoggedInEvent.subscribe(
      (isLoggedIn) => this.isLoggedIn = isLoggedIn
    );

    if (this.isLoggedIn) {
      this.instrumentService.instrumentsChanged.subscribe(
        (instruments) => {
          this.instruments = instruments
          this.selectedInstrument = instruments[0]
        })

      this.listService.getLists().subscribe(
        (lists) => this.lists = lists)

      this.listService.listsChanged.subscribe(
        (lists) => this.lists = lists)
    }
  }

  ngAfterViewInit(): void {
    this._inputElement.nativeElement.focus();
  }

  selectInstrument(instrument) {
    this.selectedInstrument = instrument;
  }

  selectComponent(data, type) {
    switch (type) {
      case 'instrument': this.selectedInstrument = data; break;
      case 'baseSound': this.selectedChord.baseSound = data; break;
      case 'baseType': this.selectedChord.baseType = data; break;
      case 'chordType': this.selectedChord.chordType = data; break;
      case 'capo': this.selectedChord.capo = data; break;
      case 'rootNote': 
        if (data) {
          this.selectedChord.rootNote = data;
        } else {
          this.selectedChord.rootNote = null;
        }
        break;

      default: console.log();
    }
  }

  fillInputWithExample() {
    this._inputElement.nativeElement.value = this.randomExample
    this.chordAnalyze(this.randomExample);
    this.randomExample = this.generateRandomExample();
  }

  login() {
    this.authService.login();
  }

  chordAnalyze(text: String) {
    if (!text) {
      this.selectedChord = null;
    } else {
      text = text.split('/').join('>');
      text = text.split('.').join('<');

      // Type example: Cmaj7/C.2
      this.chordService.chordTextAnalyze(text).subscribe(
        (chord) => {
          this.selectedChord = chord
          this.chordService.getSoundsByChordComponents(this.selectedChord.baseSound.name, this.selectedChord.baseType.name, this.selectedChord.chordType.name).subscribe(
            (rootNotes) => {
              this.rootNotes = rootNotes
            console.log(this.selectedChord)});
        }
      );
     }
  }

  getChordCatches() {
    let rootNote = this.selectedChord.rootNote !== null ? this.selectedChord.rootNote.name : undefined;
    let pathVariables = this.selectedInstrument.instrumentToken + "/" + this.selectedChord.baseSound.name + "/" + this.selectedChord.baseType.name + "/" + 
      this.selectedChord.chordType.name + "/" + rootNote + "/" + this.selectedChord.capo;
    console.log(pathVariables);
    this.chordService.changePath(pathVariables);
    this.scrollService.scrollToChords();
  }


  private generateRandomExample(): String {
    let randomIndex = Math.floor(Math.random() * 12)

    return this.chordComponent.baseSounds[randomIndex].label + this.chordComponent.baseTypes[Math.floor(Math.random() * 3)].name + this.chordComponent.chordTypes[randomIndex].label + 
      "/" + this.chordComponent.baseSounds[randomIndex].label + "." +  Math.floor(Math.random() * 5 + 1)
  }

}
