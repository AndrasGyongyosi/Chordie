import { Component, OnInit } from '@angular/core';
import { ListService } from 'src/app/services/list.service';
import { List } from 'src/app/models/list.model';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { DialogService } from 'src/app/services/dialog-service';

@Component({
  selector: 'app-list-selector',
  templateUrl: './list-selector.component.html',
  styleUrls: ['./list-selector.component.scss']
})
export class ListSelectorComponent implements OnInit {

  public lists: List[];
  public isLoggedIn;
  public selectedList;
  
  constructor(private listService: ListService, private authService: AuthenticationService, private dialogService: DialogService) { }

  ngOnInit(): void {
    this.isLoggedIn = localStorage.getItem("userIdToken");
    this.authService.isLoggedInEvent.subscribe(
      (isLoggedIn) => this.isLoggedIn = isLoggedIn
    );

    if (this.isLoggedIn) {
      this.listService.getLists().subscribe(
        (lists) => this.lists = lists)
  
      this.listService.listsChanged.subscribe(
        (lists) => this.lists = lists)
    }

    this.getSelectedList();
  }

  selectList(list: List) {
    if (list) {
      localStorage.setItem("listToken", list.listToken);
      this.getSelectedList();
    } else {
      localStorage.removeItem("listToken");
    }
  }

  openNewListDialog() {
    this.dialogService.openEditOrNewListDialog(null).subscribe(
      (data) => {
        if (data) {     
          if (data.action == "accept") {
            this.listService.addNewList(data.name).subscribe(
              () => {
                this.listService.getLists().subscribe(
                  (lists) => {
                    this.lists = lists;
                    this.listService.listsChanged.emit(lists);
                  })
              })
          }
        } 
      })
  }

  private getSelectedList() {
    if (localStorage.getItem("listToken")) {
      this.listService.getListByToken(localStorage.getItem("listToken")).subscribe(
        (list) =>  {
          this.selectedList = list
          this.listService.selectedListChanged.emit(list)
        })
    }
  }
}
