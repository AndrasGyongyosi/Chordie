import { Catch } from './catch.model';

export interface CatchResult {
    catches: Catch[],
    bundDif: number,
    chord: string,
    instrument: string
}