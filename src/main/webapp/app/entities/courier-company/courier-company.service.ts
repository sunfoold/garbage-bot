import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICourierCompany } from 'app/shared/model/courier-company.model';

type EntityResponseType = HttpResponse<ICourierCompany>;
type EntityArrayResponseType = HttpResponse<ICourierCompany[]>;

@Injectable({ providedIn: 'root' })
export class CourierCompanyService {
  public resourceUrl = SERVER_API_URL + 'api/courier-companies';

  constructor(protected http: HttpClient) {}

  create(courierCompany: ICourierCompany): Observable<EntityResponseType> {
    return this.http.post<ICourierCompany>(this.resourceUrl, courierCompany, { observe: 'response' });
  }

  update(courierCompany: ICourierCompany): Observable<EntityResponseType> {
    return this.http.put<ICourierCompany>(this.resourceUrl, courierCompany, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICourierCompany>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICourierCompany[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
