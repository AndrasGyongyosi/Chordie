import { Component, OnInit, Inject } from '@angular/core';
import { InstrumentService } from 'src/app/services/instrument.service';
import { Instrument } from 'src/app/model/instrument.model';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ChordService } from 'src/app/services/chord.service';
import { ChordProperty } from 'src/app/model/ChordProperty.model';
import { CommonDialogService } from 'src/app/services/common-dialog-service';
import { ChordComponent } from 'src/app/model/ChordComponent.model';

@Component({
  selector: 'app-instruments-and-lists',
  templateUrl: './instruments-and-lists.component.html',
  styleUrls: ['./instruments-and-lists.component.scss']
})
export class InstrumentsAndListsComponent implements OnInit {

  chordComponents: ChordComponent;
  panelOpenState = false;
  instruments: Instrument[] = [];

  constructor(private instrumentService: InstrumentService, private chordService: ChordService, private commonDialogService: CommonDialogService) { }

  ngOnInit(): void {
    this.instrumentService.getInstrumentsByUser().subscribe(
      (instruments) => this.instruments = instruments
    );

    this.chordService.getChordComponents().subscribe(
      (chordComponent) => this.chordComponents = chordComponent
    );
  }

  openDialog() {
    this.commonDialogService.openDialog(this.chordComponents.baseSounds).subscribe(
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
    this.commonDialogService.openEditInstrumentDialog(instrument, this.chordComponents.baseSounds).subscribe(
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

}

@Component({
  selector: 'edit-insturment-dialog',
  templateUrl: 'edit-instrument-dialog.html',
  styleUrls: ['./edit-instrument-dialog.scss']
})
export class EditInstrumentDialog implements OnInit {

  selectedString: string;
  chordComponents: ChordComponent;
  temp_selectedStrings: ChordProperty[];

  constructor(public dialogRef: MatDialogRef<EditInstrumentDialog>,
    @Inject(MAT_DIALOG_DATA) public data: any, private instrumentService: InstrumentService, private chordService: ChordService) {}

  ngOnInit(): void {
    this.chordService.getChordComponents().subscribe(
      (data) => this.chordComponents = data
    );

    console.log("ngoninit")
    console.log(this.data.selectedStrings)
    this.temp_selectedStrings = this.data.selectedStrings;
  }

  addString() {
    console.log(this.selectedString);

    let validSelectedString: ChordProperty;
    let name = this.chordComponents.baseSounds.find(c => c.label == this.selectedString).name;
    validSelectedString = ({ "label": this.selectedString, "name": name });

    console.log(validSelectedString)
    this.data.selectedStrings.push(validSelectedString);
  }

  Accept() {
    console.log("Accept");
    this.data.selectedStrings = this.temp_selectedStrings;
  }

  onNoClick(): void {
    console.log("close")
    this.dialogRef.close();
  }

  deleteInstrument(instrumentToken) {
    console.log("DELETE" + instrumentToken);
    this.dialogRef.close();
    this.instrumentService.deleteInstrument(instrumentToken).subscribe(
      (data) => console.log(data)
    );   
  }

  removeSelectedString(selectedStringIndex: number) { 
    if (!this.data.isPublic) {
      console.log("deleted: " + selectedStringIndex);
      console.log(this.data.selectedStrings);
      this.temp_selectedStrings.splice(selectedStringIndex, 1);
      console.log(this.data.selectedStrings);
    }
  }

}
