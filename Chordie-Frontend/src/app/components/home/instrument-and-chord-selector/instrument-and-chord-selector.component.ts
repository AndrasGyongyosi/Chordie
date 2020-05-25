import { Component, OnInit } from '@angular/core';
import { ChordService } from 'src/app/services/chord.service';
import { ChordComponent } from 'src/app/model/ChordComponent.model';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-instrument-and-chord-selector',
  templateUrl: './instrument-and-chord-selector.component.html',
  styleUrls: ['./instrument-and-chord-selector.component.scss']
})
export class InstrumentAndChordSelectorComponent implements OnInit {

  public chordComponent: ChordComponent;
  public selectedBaseSound;
  public selectedBaseType;
  public selectedChordType;

  public isLoggedIn;

  constructor(private chordService: ChordService, private authService: AuthenticationService) { }

  ngOnInit(): void {
    this.chordService.getChordComponents().subscribe(
      data => this.chordComponent = data
    );

    this.isLoggedIn = localStorage.getItem("userIdToken");
    this.authService.isLoggedInEvent.subscribe(
      (isLoggedIn) => this.isLoggedIn = isLoggedIn
    );
  }

  selectCurrentBaseSound(baseSound) {
    this.selectedBaseSound = baseSound;
  }

  selectedCurrentBaseType(baseType) {
    this.selectedBaseType = baseType;
  }

  selectedCurrentChordType(chordType) {
    this.selectedChordType = chordType;
  }

  login() {
    this.authService.login();
  }

}
