import { Catch } from './catch.model';

export interface CatchResult {
    catches: Catch[],
    bundDif: number,
    chord: string,
    capo: number,
    rootNote: string,
    instrument: string
}