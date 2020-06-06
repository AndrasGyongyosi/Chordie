import { Component, OnInit, Inject } from '@angular/core';
import { ChordComponent } from 'src/app/models/chordComponent.model';
import { ChordProperty } from 'src/app/models/chordProperty.model';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { InstrumentService } from 'src/app/services/instrument.service';
import { ChordService } from 'src/app/services/chord.service';

@Component({
    selector: 'edit-insturment-dialog',
    templateUrl: 'edit-instrument-dialog.component.html',
    styleUrls: ['./edit-instrument-dialog.component.scss']
  })
  export class EditInstrumentDialogComponent implements OnInit {
  
    selectedString: string;
    chordComponents: ChordComponent;
    temp_selectedStrings: ChordProperty[];
  
    constructor(public dialogRef: MatDialogRef<EditInstrumentDialogComponent>,
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