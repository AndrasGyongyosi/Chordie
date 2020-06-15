import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ChordService } from 'src/app/services/chord.service';
import { StoredCatch } from 'src/app/models/stored-catch.model';
import { ListService } from 'src/app/services/list.service';

@Component({
  selector: 'edit-or-new-list-dialog',
  templateUrl: 'edit-or-new-list-dialog.component.html',
  styleUrls: ['./edit-or-new-list-dialog.component.scss']
})
export class EditOrNewListDialogComponent implements OnInit {

  public bundsByCatch: [number?, number?, number?, number?, number?][] = [[]];


  constructor(public dialogRef: MatDialogRef<EditOrNewListDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: any, private chordService: ChordService, private listService: ListService) { }

  ngOnInit(): void {
    this.bundsByCatch = this.chordService.calculateBunds(this.data.catches);
  }

  Accept() {
    console.log("Accept");
    this.data.action = "accept";
  }

  onNoClick(): void {
    console.log("close");
    this.dialogRef.close();
  }

  deleteList() {
    this.data.action = "delete";
  }

  deleteCatch(storedCatch: StoredCatch) {
    this.listService.deleteCatch(storedCatch.listToken).subscribe();
  }

}