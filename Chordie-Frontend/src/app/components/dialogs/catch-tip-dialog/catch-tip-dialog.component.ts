import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
    selector: 'catch-tip-dialog.component',
    templateUrl: 'catch-tip-dialog.component.html',
    styleUrls: ['./catch-tip-dialog.component.scss']
})

export class CatchTipDialogComponent implements OnInit {

    yellow = '';

    constructor(public dialogRef: MatDialogRef<CatchTipDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any) { }

    ngOnInit(): void {

    }

    onNoClick(): void {
        console.log("close")
        this.dialogRef.close();
    }

    changeTipImg(yellow) {
        if (yellow == "") {
            this.yellow = "_yellow";
        }
        else {
            this.yellow = '';
        }
    }
}