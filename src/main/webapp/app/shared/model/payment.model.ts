import { Moment } from 'moment';
import { IAppUser } from 'app/shared/model/app-user.model';
import { PaymentStatus } from 'app/shared/model/enumerations/payment-status.model';

export interface IPayment {
  id?: number;
  paymentDate?: Moment;
  status?: PaymentStatus;
  value?: number;
  user?: IAppUser;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public paymentDate?: Moment,
    public status?: PaymentStatus,
    public value?: number,
    public user?: IAppUser
  ) {}
}
