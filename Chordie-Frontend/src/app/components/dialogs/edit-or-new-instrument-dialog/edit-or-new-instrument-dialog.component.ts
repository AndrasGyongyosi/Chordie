import { Component, OnInit, Inject } from "@angular/core";
import { ChordComponent } from 'src/app/models/chordComponent.model';
import { ChordProperty } from 'src/app/models/chordProperty.model';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { InstrumentService } from 'src/app/services/instrument.service';
import { ChordService } from 'src/app/services/chord.service';


@Component({
    selector: 'edit-or-new-insturment-dialog',
    templateUrl: 'edit-or-new-instrument-dialog.component.html',
    styleUrls: ['./edit-or-new-instrument-dialog.component.scss']
  })
  export class EditOrNewInstrumentDialogComponent implements OnInit {
  
    selectedString: string;
    chordComponents: ChordComponent;
    temp_selectedStrings: ChordProperty[];
  
    constructor(public dialogRef: MatDialogRef<EditOrNewInstrumentDialogComponent>,
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
      this.data.action = "accept";
      this.data.selectedStrings = this.temp_selectedStrings;
    }
  
    onNoClick(): void {
      console.log("close");
      this.data.action = "reject";
      this.dialogRef.close();
    }
  
    deleteInstrument() {
      this.data.action = "delete"    
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