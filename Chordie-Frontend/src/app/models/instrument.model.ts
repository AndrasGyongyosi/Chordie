import { ChordProperty } from './chordProperty.model';

export interface Instrument {
    name: string,
    instrumentToken: string,
    bundNumber: number,
    isPublic: boolean,
    maxBundDif: number,
    strings: ChordProperty[]
}