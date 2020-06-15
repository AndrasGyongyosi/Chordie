import { Catch } from './catch.model';
import { ChordModel } from './chord.model';

export interface StoredCatch extends Catch {
    chord: ChordModel,
    instrument: string
}