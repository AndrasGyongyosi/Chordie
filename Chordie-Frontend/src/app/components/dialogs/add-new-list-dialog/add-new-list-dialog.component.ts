import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
    selector: 'add-new-list-dialog',
    templateUrl: 'add-new-list-dialog.component.html',
    styleUrls: ['./add-new-list-dialog.component.scss']
})
export class AddNewListDialogComponent implements OnInit {


    constructor(public dialogRef: MatDialogRef<AddNewListDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: any) { }

    ngOnInit(): void {
    }

}