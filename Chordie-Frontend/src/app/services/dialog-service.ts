import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { Instrument } from '../models/instrument.model';
import { CatchTipDialogComponent } from '../components/dialogs/catch-tip-dialog/catch-tip-dialog.component';
import { EditOrNewListDialogComponent } from '../components/dialogs/edit-or-new-list-dialog/edit-or-new-list-dialog.component';
import { List } from '../models/list.model';
import { EditOrNewInstrumentDialogComponent } from '../components/dialogs/edit-or-new-instrument-dialog/edit-or-new-instrument-dialog.component';

@Injectable({
  providedIn: 'root'
})
export class DialogService {

  constructor(public dialog: MatDialog) { }

  openEditOrNewInstrumentDialog(instrument: Instrument, baseSounds): Observable<any> {

    const dialogRef = this.dialog.open(EditOrNewInstrumentDialogComponent, {
      data: {
        baseSounds: baseSounds,
        instrumentName: instrument ? instrument.name : '',
        maxBundDif: instrument ? instrument.maxBundDif : null,
        bundNumber: instrument ? instrument.bundNumber : null,
        selectedStrings: instrument ? instrument.strings : [],
        instrumentToken: instrument ? instrument.instrumentToken : '',
        isPublic: instrument ? instrument.isPublic : undefined,
        action: ''
      }
    });

    return dialogRef.afterClosed();
  }

  openCatchTipDialog(): Observable<any> {
    const dialogRef = this.dialog.open(CatchTipDialogComponent);
    return dialogRef.afterClosed();
  }

  openEditOrNewListDialog(list: List): Observable<any> {
    const dialogRef = this.dialog.open(EditOrNewListDialogComponent, {
      data: {
        name: '',
        listToken: list ? list.listToken : '' ,
        action: ''
      }
    });
    return dialogRef.afterClosed();
  }

}
