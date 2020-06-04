import { ChordProperty } from './ChordProperty.model';

export interface Instrument {
    name: string,
    token: string,
    bundNumber: string,
    isPublic: boolean,
    maxBundDif: string,
    strings: ChordProperty[]
}