export interface ICourierCompany {
  id?: number;
  name?: string;
  inn?: string;
  ogrn?: string;
  address?: string;
  phone?: string;
  isActive?: boolean;
}

export class CourierCompany implements ICourierCompany {
  constructor(
    public id?: number,
    public name?: string,
    public inn?: string,
    public ogrn?: string,
    public address?: string,
    public phone?: string,
    public isActive?: boolean
  ) {
    this.isActive = this.isActive || false;
  }
}
