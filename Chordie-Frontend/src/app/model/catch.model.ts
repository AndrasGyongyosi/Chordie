import { StringCatch } from './stringCatch.model';

export interface Catch {
    stringCatches: StringCatch[],
    chord: string,
    instrument: string,
    capo: number
}