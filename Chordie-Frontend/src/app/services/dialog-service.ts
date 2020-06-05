import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { Instrument } from '../model/instrument.model';
import { CatchTipDialogComponent } from '../components/dialogs/catch-tip-dialog/catch-tip-dialog.component';
import { EditInstrumentDialogComponent } from '../components/dialogs/edit-instrument-dialog/edit-instrument-dialog.component';
import { AddNewListDialogComponent } from '../components/dialogs/add-new-list-dialog/add-new-list-dialog.component';

@Injectable({
  providedIn: 'root'
})
export class DialogService {

  constructor(public dialog: MatDialog) { }

  openEditOrNewInstrumentDialog(instrument: Instrument, baseSounds): Observable<any> {

    const dialogRef = this.dialog.open(EditInstrumentDialogComponent, {
      data: {
        baseSounds: baseSounds,
        instrumentName: instrument ? instrument.name : '',
        maxBundDif: instrument ? instrument.maxBundDif : '',
        bundNumber: instrument ? instrument.bundNumber : '',
        selectedStrings: instrument ? instrument.strings : [],
        instrumentToken: instrument ? instrument.token : '',
        isPublic: instrument ? instrument.isPublic : undefined
      }
    });

    return dialogRef.afterClosed();
  }

  openCatchTipDialog(): Observable<any> {
    const dialogRef = this.dialog.open(CatchTipDialogComponent);
    return dialogRef.afterClosed();
  }

  openAddNewListDialog(): Observable<any> {
    const dialogRef = this.dialog.open(AddNewListDialogComponent);
    return dialogRef.afterClosed();
  }

}
