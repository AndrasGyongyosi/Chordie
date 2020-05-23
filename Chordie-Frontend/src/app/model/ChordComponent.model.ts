import { ChordProperty } from './ChordProperty.model';

export interface ChordComponent {
    baseSounds: ChordProperty[]; 
    baseTypes: ChordProperty[]; 
    chordTypes: ChordProperty[]; 
}