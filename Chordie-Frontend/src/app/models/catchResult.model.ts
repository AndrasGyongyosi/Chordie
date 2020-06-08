import { Catch } from './catch.model';
import { ChordProperty } from './chordProperty.model';

export interface CatchResult {
    catches: Catch[],
    bundDif: number,
    chord: string,
    capo: number,
    rootNote: ChordProperty,
    instrument: string
}