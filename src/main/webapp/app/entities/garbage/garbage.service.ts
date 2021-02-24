import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IGarbage } from 'app/shared/model/garbage.model';

type EntityResponseType = HttpResponse<IGarbage>;
type EntityArrayResponseType = HttpResponse<IGarbage[]>;

@Injectable({ providedIn: 'root' })
export class GarbageService {
  public resourceUrl = SERVER_API_URL + 'api/garbage';

  constructor(protected http: HttpClient) {}

  create(garbage: IGarbage): Observable<EntityResponseType> {
    return this.http.post<IGarbage>(this.resourceUrl, garbage, { observe: 'response' });
  }

  update(garbage: IGarbage): Observable<EntityResponseType> {
    return this.http.put<IGarbage>(this.resourceUrl, garbage, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGarbage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGarbage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
