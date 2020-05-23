import { Component, OnInit } from '@angular/core';
import { ChordComponent } from '../model/ChordComponent.model';
import { ChordService } from '../services/chord.service';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.scss']
})
export class AboutComponent implements OnInit {

  public chords: ChordComponent;

  constructor(private chordService: ChordService) { }

  ngOnInit(): void {
    this.chordService.getChordComponents().subscribe(
      c => this.chords = c);
  }

}
