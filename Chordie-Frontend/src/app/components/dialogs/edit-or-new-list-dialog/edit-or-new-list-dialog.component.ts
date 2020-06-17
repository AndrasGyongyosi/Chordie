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
  public customHeight = '250px';


  constructor(public dialogRef: MatDialogRef<EditOrNewListDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: any, 
    private chordService: ChordService, private listService: ListService) { }

  ngOnInit(): void {
    console.log(this.data.catches)
    if (this.data.catches.length != 0) {
      console.log(this.data.catches.length)
      this.bundsByCatch = this.chordService.calculateBundsForStoredCatch(this.data.catches);
      console.log(this.bundsByCatch)
      if (this.data.catches.length > 1) {
        this.customHeight = '455px';
      }
    }
  }

  Accept() {
    console.log("Accept");
    this.data.action = "accept";
  }

  onNoClick(): void {
    console.log("close");
    this.data.deletedCatches = [];
    this.data.action = "noclick";
    this.listService.getLists().subscribe(
      (lists) => this.listService.listsChanged.emit(lists))
  }

  deleteList() {
    if (confirm("Are you sure to want to delete?"))
      this.data.action = "delete";
  }

  deleteCatch(storedCatch: StoredCatch) {
    console.log(storedCatch)

    const index: number = this.data.catches.indexOf(storedCatch);
    if (index !== -1) {
        this.data.catches.splice(index, 1);
        this.bundsByCatch.splice(index, 1);
    }  

    console.log(index)

    this.data.deletedCatches.push(storedCatch)
  }
}