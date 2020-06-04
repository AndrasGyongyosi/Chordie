import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { EditInstrumentDialog } from '../components/instruments-and-lists/instruments-and-lists.component';
import { Instrument } from '../model/instrument.model';
import { CatchTipDialogComponent } from '../components/chords/chords.component';

@Injectable({
  providedIn: 'root'
})
export class CommonDialogService {

  constructor(public dialog: MatDialog) { }

  openDialog(baseSounds): Observable<any> {

    const dialogRef = this.dialog.open(EditInstrumentDialog, {
      data: {
        baseSounds: baseSounds,
        instrumentName: '',
        maxBundDif: '',
        bundNumber: '',
        selectedStrings: []
      }
    });

    return dialogRef.afterClosed();
  }

  openEditInstrumentDialog(instrument: Instrument, baseSounds): Observable<any> {

    const dialogRef = this.dialog.open(EditInstrumentDialog, {
      data: {
        baseSounds: baseSounds,
        instrumentName: instrument.name,
        maxBundDif: instrument.maxBundDif,
        bundNumber: instrument.bundNumber,
        selectedStrings: instrument.strings,
        instrumentToken: instrument.token,
        isPublic: instrument.isPublic
      }
    });

    return dialogRef.afterClosed();
  }

  openCatchTipDialog(): Observable<any> {
    const dialogRef = this.dialog.open(CatchTipDialogComponent, {
      data: {
        
      }
    });



    return dialogRef.afterClosed();
  }

}
