export interface IGarbage {
  id?: number;
  pockets?: number;
  hugeThings?: number;
}

export class Garbage implements IGarbage {
  constructor(public id?: number, public pockets?: number, public hugeThings?: number) {}
}
