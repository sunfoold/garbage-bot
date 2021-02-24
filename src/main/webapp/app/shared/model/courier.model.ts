import { Moment } from 'moment';
import { ICourierCompany } from 'app/shared/model/courier-company.model';

export interface ICourier {
  id?: number;
  name?: string;
  phoneNumber?: string;
  photoUrl?: string;
  telegramChatId?: number;
  joinDate?: Moment;
  balance?: number;
  company?: ICourierCompany;
}

export class Courier implements ICourier {
  constructor(
    public id?: number,
    public name?: string,
    public phoneNumber?: string,
    public photoUrl?: string,
    public telegramChatId?: number,
    public joinDate?: Moment,
    public balance?: number,
    public company?: ICourierCompany
  ) {}
}
