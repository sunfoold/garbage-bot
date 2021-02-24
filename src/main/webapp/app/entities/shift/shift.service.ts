import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IShift } from 'app/shared/model/shift.model';

type EntityResponseType = HttpResponse<IShift>;
type EntityArrayResponseType = HttpResponse<IShift[]>;

@Injectable({ providedIn: 'root' })
export class ShiftService {
  public resourceUrl = SERVER_API_URL + 'api/shifts';

  constructor(protected http: HttpClient) {}

  create(shift: IShift): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(shift);
    return this.http
      .post<IShift>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(shift: IShift): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(shift);
    return this.http
      .put<IShift>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IShift>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IShift[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(shift: IShift): IShift {
    const copy: IShift = Object.assign({}, shift, {
      shiftPlanStartDate: shift.shiftPlanStartDate && shift.shiftPlanStartDate.isValid() ? shift.shiftPlanStartDate.toJSON() : undefined,
      shiftFactStartDate: shift.shiftFactStartDate && shift.shiftFactStartDate.isValid() ? shift.shiftFactStartDate.toJSON() : undefined,
      shiftPlanEndDate: shift.shiftPlanEndDate && shift.shiftPlanEndDate.isValid() ? shift.shiftPlanEndDate.toJSON() : undefined,
      shiftFactEndDate: shift.shiftFactEndDate && shift.shiftFactEndDate.isValid() ? shift.shiftFactEndDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.shiftPlanStartDate = res.body.shiftPlanStartDate ? moment(res.body.shiftPlanStartDate) : undefined;
      res.body.shiftFactStartDate = res.body.shiftFactStartDate ? moment(res.body.shiftFactStartDate) : undefined;
      res.body.shiftPlanEndDate = res.body.shiftPlanEndDate ? moment(res.body.shiftPlanEndDate) : undefined;
      res.body.shiftFactEndDate = res.body.shiftFactEndDate ? moment(res.body.shiftFactEndDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((shift: IShift) => {
        shift.shiftPlanStartDate = shift.shiftPlanStartDate ? moment(shift.shiftPlanStartDate) : undefined;
        shift.shiftFactStartDate = shift.shiftFactStartDate ? moment(shift.shiftFactStartDate) : undefined;
        shift.shiftPlanEndDate = shift.shiftPlanEndDate ? moment(shift.shiftPlanEndDate) : undefined;
        shift.shiftFactEndDate = shift.shiftFactEndDate ? moment(shift.shiftFactEndDate) : undefined;
      });
    }
    return res;
  }
}
