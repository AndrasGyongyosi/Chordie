import { Component, OnInit } from '@angular/core';
import { ListService } from 'src/app/services/list.service';
import { List } from 'src/app/models/list.model';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-list-selector',
  templateUrl: './list-selector.component.html',
  styleUrls: ['./list-selector.component.scss']
})
export class ListSelectorComponent implements OnInit {

  public lists: List[];
  public isLoggedIn;
  
  constructor(public listService: ListService, private authService: AuthenticationService) { }

  ngOnInit(): void {
    this.listService.getLists().subscribe(
      (lists) => this.lists = lists)

    this.listService.listsChanged.subscribe(
      (lists) => this.lists = lists)

      this.isLoggedIn = localStorage.getItem("userIdToken");
    this.authService.isLoggedInEvent.subscribe(
      (isLoggedIn) => this.isLoggedIn = isLoggedIn
    );
  }

  selectList(list: List) {
    if (list) {
      this.listService.selectedList = list;
    } else {
      this.listService.selectedList = null;
    }
  }
}
