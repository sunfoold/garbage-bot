import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICourier } from 'app/shared/model/courier.model';

type EntityResponseType = HttpResponse<ICourier>;
type EntityArrayResponseType = HttpResponse<ICourier[]>;

@Injectable({ providedIn: 'root' })
export class CourierService {
  public resourceUrl = SERVER_API_URL + 'api/couriers';

  constructor(protected http: HttpClient) {}

  create(courier: ICourier): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courier);
    return this.http
      .post<ICourier>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(courier: ICourier): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courier);
    return this.http
      .put<ICourier>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICourier>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICourier[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(courier: ICourier): ICourier {
    const copy: ICourier = Object.assign({}, courier, {
      joinDate: courier.joinDate && courier.joinDate.isValid() ? courier.joinDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.joinDate = res.body.joinDate ? moment(res.body.joinDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((courier: ICourier) => {
        courier.joinDate = courier.joinDate ? moment(courier.joinDate) : undefined;
      });
    }
    return res;
  }
}
