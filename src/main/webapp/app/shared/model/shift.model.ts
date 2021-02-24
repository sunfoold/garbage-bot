import { Moment } from 'moment';

export interface IShift {
  id?: number;
  shiftPlanStartDate?: Moment;
  shiftFactStartDate?: Moment;
  shiftPlanEndDate?: Moment;
  shiftFactEndDate?: Moment;
  prepaid?: boolean;
}

export class Shift implements IShift {
  constructor(
    public id?: number,
    public shiftPlanStartDate?: Moment,
    public shiftFactStartDate?: Moment,
    public shiftPlanEndDate?: Moment,
    public shiftFactEndDate?: Moment,
    public prepaid?: boolean
  ) {
    this.prepaid = this.prepaid || false;
  }
}
