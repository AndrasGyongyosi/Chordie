import { Component, OnInit } from '@angular/core';
import { InstrumentService } from 'src/app/services/instrument.service';
import { Instrument } from 'src/app/model/instrument.model';
import { ChordService } from 'src/app/services/chord.service';
import { DialogService } from 'src/app/services/dialog-service';
import { ChordComponent } from 'src/app/model/chordComponent.model';

@Component({
  selector: 'app-instruments-and-lists',
  templateUrl: './instruments-and-lists.component.html',
  styleUrls: ['./instruments-and-lists.component.scss']
})
export class InstrumentsAndListsComponent implements OnInit {

  chordComponents: ChordComponent;
  panelOpenState = false;
  instruments: Instrument[] = [];

  constructor(private instrumentService: InstrumentService, private chordService: ChordService, private dialogService: DialogService) { }

  ngOnInit(): void {
    this.instrumentService.getInstrumentsByUser().subscribe(
      (instruments) => this.instruments = instruments
    );

    this.chordService.getChordComponents().subscribe(
      (chordComponent) => this.chordComponents = chordComponent
    );
  }

  openNewInstrumentDialog() {
    this.dialogService.openEditOrNewInstrumentDialog(undefined, this.chordComponents.baseSounds).subscribe(
      (data) => {
        console.log(data)

        if (data) {
          this.instrumentService.addNewInstrumentForUser(data.instrumentName, data.maxBundDif, data.bundNumber, data.selectedStrings).subscribe(
            () => {
              this.instrumentService.getInstrumentsByUser().subscribe(
              (instruments) => this.instruments = instruments
              );
          }
          );  
        }
      }
    );
  }

  openEditInstrumentDialog(instrument: Instrument) {
    console.log(instrument)
    this.dialogService.openEditOrNewInstrumentDialog(instrument, this.chordComponents.baseSounds).subscribe(
      (data) => {
        console.log(data)
        console.log()

        if (data) {
          console.log("in IF")
          this.instrumentService.editInstrumentByUser(instrument.token, data.instrumentName, data.maxBundDif.toString(), data.bundNumber.toString(), data.selectedStrings).subscribe(
            () => {
              console.log("in in IF")
              this.instrumentService.getInstrumentsByUser().subscribe(
              (instruments) => this.instruments = instruments
              );
          }
          );
        }
      });
  }

  openAddNewListDialog() {
    this.dialogService.openAddNewListDialog().subscribe();
  }

}