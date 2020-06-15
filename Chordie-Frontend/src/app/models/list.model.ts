import { StoredCatch } from './stored-catch.model';

export interface List {
    name: string,
    listToken: string,
    userToken: string,
    catches: StoredCatch[]
}