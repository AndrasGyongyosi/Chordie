import { ChordProperty } from './chordProperty.model';

export interface ChordComponent {
    baseSounds: ChordProperty[]; 
    baseTypes: ChordProperty[]; 
    chordTypes: ChordProperty[]; 
}