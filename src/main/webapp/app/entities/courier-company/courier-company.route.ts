import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICourierCompany, CourierCompany } from 'app/shared/model/courier-company.model';
import { CourierCompanyService } from './courier-company.service';
import { CourierCompanyComponent } from './courier-company.component';
import { CourierCompanyDetailComponent } from './courier-company-detail.component';
import { CourierCompanyUpdateComponent } from './courier-company-update.component';

@Injectable({ providedIn: 'root' })
export class CourierCompanyResolve implements Resolve<ICourierCompany> {
  constructor(private service: CourierCompanyService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICourierCompany> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((courierCompany: HttpResponse<CourierCompany>) => {
          if (courierCompany.body) {
            return of(courierCompany.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CourierCompany());
  }
}

export const courierCompanyRoute: Routes = [
  {
    path: '',
    component: CourierCompanyComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'garbageApp.courierCompany.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CourierCompanyDetailComponent,
    resolve: {
      courierCompany: CourierCompanyResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'garbageApp.courierCompany.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourierCompanyUpdateComponent,
    resolve: {
      courierCompany: CourierCompanyResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'garbageApp.courierCompany.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CourierCompanyUpdateComponent,
    resolve: {
      courierCompany: CourierCompanyResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'garbageApp.courierCompany.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
