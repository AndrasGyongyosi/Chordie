import { Component, OnInit } from '@angular/core';
import { InstrumentService } from 'src/app/services/instrument.service';
import { Instrument } from 'src/app/models/instrument.model';
import { ChordService } from 'src/app/services/chord.service';
import { DialogService } from 'src/app/services/dialog-service';
import { ChordComponent } from 'src/app/models/chordComponent.model';
import { List } from 'src/app/models/list.model';
import { ListService } from 'src/app/services/list.service';

@Component({
  selector: 'app-instruments-and-lists',
  templateUrl: './instruments-and-lists.component.html',
  styleUrls: ['./instruments-and-lists.component.scss']
})
export class InstrumentsAndListsComponent implements OnInit {

  chordComponents: ChordComponent;
  panelOpenStateInstrument = false;
  panelOpenStateList = false;
  instruments: Instrument[] = [];
  instrumentHeight;
  lists: List[] = [];
  listHeight;

  constructor(private instrumentService: InstrumentService, private chordService: ChordService, private dialogService: DialogService, private listService: ListService) { }

  ngOnInit(): void {
    this.instrumentService.getInstrumentsByUser().subscribe(
      (instruments) => {
        this.instruments = instruments
        this.instrumentHeight = instruments.length * 70 + 'px'
      }
    );

    this.chordService.getChordComponents().subscribe(
      (chordComponent) => this.chordComponents = chordComponent
    );

    if (localStorage.getItem("userIdToken")) {
      this.listService.getLists().subscribe(
        (lists) => {
          this.lists = lists
          this.listHeight = lists.length * 70 + 'px'
        })

      this.listService.listsChanged.subscribe(
        (lists) => {
          console.log("lists changed")
          this.lists = lists
          this.listHeight = lists.length * 70 + 'px'
        })

      this.instrumentService.instrumentsChanged.subscribe(
        (instruments) => {
          this.instruments = instruments
          this.instrumentHeight = instruments.length * 70 + 'px'
        })

    }
  }

  openNewInstrumentDialog() {
    this.dialogService.openEditOrNewInstrumentDialog(undefined, this.chordComponents.baseSounds).subscribe(
      (data) => {
        console.log(data)

        if (data) {
          this.instrumentService.addNewInstrumentForUser(data.instrumentName, data.maxBundDif, data.bundNumber, data.selectedStrings).subscribe(
            () => {
              this.instrumentService.getInstrumentsByUser().subscribe(
              (instruments) => {
                this.instruments = instruments
                this.instrumentHeight = instruments.length * 70 + 'px'
              }
              );
          }
          );  
        }
      }
    );
  }

  openEditInstrumentDialog(instrument: Instrument) {
    console.log(instrument)
    this.dialogService.openEditOrNewInstrumentDialog(instrument, this.chordComponents.baseSounds).subscribe(
      (data) => {
        if (data) {

          if (data.action == "accept") {
            this.instrumentService.editInstrumentByUser(instrument.instrumentToken, data.instrumentName, data.maxBundDif, data.bundNumber, data.selectedStrings).subscribe(
              () => {
                this.instrumentService.getInstrumentsByUser().subscribe(
                  (instruments) => {
                    this.instruments = instruments
                    this.instrumentHeight = instruments.length * 70 + 'px'
                  })
              });
          }

          if (data.action == "delete") {
            this.instrumentService.deleteInstrument(data.instrumentToken).subscribe(
              () => {
                this.instrumentService.getInstrumentsByUser().subscribe(
                  (instruments) => {
                    this.instruments = instruments
                    this.instrumentHeight = instruments.length * 70 + 'px'
                  })
              })
          }
          
        } 
        else {
          // Call this, when user click outside the dialog for avoid inconsistence data
          this.instrumentService.getInstrumentsByUser().subscribe(
            (instrument) => this.instrumentService.instrumentsChanged.emit(instrument))
        }
      });
  }

  openNewListDialog() {
    this.dialogService.openEditOrNewListDialog(null).subscribe(
      (data) => {
        if (data) {
          
          if (data.action == "accept") {
            console.log(data)
            this.listService.addNewList(data.name).subscribe(
              () => {
                this.listService.getLists().subscribe(
                  (lists) => {
                    this.lists = lists;
                    console.log(this.lists)
                    this.listHeight = lists.length * 70 + 'px'
                    this.listService.listsChanged.emit(lists);
                  })
              })
        }
      }    
    });
  }

  openEditListDialog(list: List) {
    this.dialogService.openEditOrNewListDialog(list).subscribe(
      (data) => {
        console.log(data)
        if (data) {
          
          if (data.action == "accept") {
            console.log(data)
            data.deletedCatches.forEach(catch_ => {
              this.listService.deleteCatch(catch_.token).subscribe(() => console.log("Catch deleted: " + catch_))
            });
            // Edit list 
        }

        if (data.action == "delete") {
          this.listService.deleteList(data.listToken).subscribe(
            () => {
              this.listService.getLists().subscribe(
                (lists) => {
                  this.lists = lists
                  this.listHeight = lists.length * 70 + 'px'
                  this.listService.listsChanged.emit(lists);
                });        
              })

            if (data.listToken == localStorage.getItem("listToken")) {
              this.listService.selectedListChanged.emit(null);
            }
        }
      } 
      // Call this, when user click outside the dialog for avoid inconsistence data
      else {
        this.listService.getLists().subscribe(
          (lists) => this.listService.listsChanged.emit(lists))
      } 
    });
  }

}