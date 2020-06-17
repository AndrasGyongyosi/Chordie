import { Catch } from './catch.model';
import { Chord } from './chord.model';

export interface StoredCatch extends Catch {
    chord?: Chord,
    instrument?: string,
    token?: string
}