import { ChordProperty } from './chordProperty.model';

export interface Chord {
    baseSound?: ChordProperty,
    baseType?: ChordProperty,
    chordType?: ChordProperty,
    rootNote?: ChordProperty,
    capo?: number
}