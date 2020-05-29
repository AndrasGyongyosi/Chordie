import { ChordProperty } from './ChordProperty.model';

export interface ChordModel {
    baseSound?: string,
    baseType?: string,
    chordType?: string,
    rootNote?: string,
    capo?: number
}