import { Component, OnInit } from '@angular/core';
import { ChordService } from 'src/app/services/chord.service';
import { ChordComponent } from 'src/app/model/ChordComponent.model';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Instrument } from 'src/app/model/instrument.model';
import { InstrumentService } from 'src/app/services/instrument.service';

@Component({
  selector: 'app-instrument-and-chord-selector',
  templateUrl: './instrument-and-chord-selector.component.html',
  styleUrls: ['./instrument-and-chord-selector.component.scss']
})
export class InstrumentAndChordSelectorComponent implements OnInit {

  public instruments: Instrument[] = [];
  public chordComponent: ChordComponent;
  
  public selectedInstrument;
  public selectedBaseSound;
  public selectedBaseType;
  public selectedChordType;
  

  public isLoggedIn;

  constructor(private chordService: ChordService, private authService: AuthenticationService, private instrumentService: InstrumentService) { }

  ngOnInit(): void {
    this.chordService.getChordComponents().subscribe(
      data => this.chordComponent = data
    );

    this.instrumentService.getInstrumentsByUser().subscribe(
      instruments => this.instruments = instruments
    );

    this.isLoggedIn = localStorage.getItem("userIdToken");
    this.authService.isLoggedInEvent.subscribe(
      (isLoggedIn) => this.isLoggedIn = isLoggedIn
    );
  }

  selectInstrument(instrument) {
    this.selectedInstrument = instrument;
  }

  selectCurrentBaseSound(baseSound) {
    this.selectedBaseSound = baseSound;
  }

  selectCurrentBaseType(baseType) {
    this.selectedBaseType = baseType;
  }

  selectCurrentChordType(chordType) {
    this.selectedChordType = chordType;
  }

  login() {
    this.authService.login();
  }

}
