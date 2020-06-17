import { Catch } from './catch.model';
import { Chord } from './chord.model';

export interface CatchResult {
    catches: Catch[],
    bundDif: number,
    chord: Chord
    instrument: string
}