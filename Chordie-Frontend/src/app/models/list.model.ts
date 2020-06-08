import { Catch } from './catch.model';

export interface List {
    name: string,
    listToken: string,
    userToken: string,
    catches: Catch[]
}