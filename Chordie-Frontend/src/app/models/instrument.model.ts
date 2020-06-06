import { ChordProperty } from './chordProperty.model';

export interface Instrument {
    name: string,
    token: string,
    bundNumber: string,
    isPublic: boolean,
    maxBundDif: string,
    strings: ChordProperty[]
}