import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICourier, Courier } from 'app/shared/model/courier.model';
import { CourierService } from './courier.service';
import { CourierComponent } from './courier.component';
import { CourierDetailComponent } from './courier-detail.component';
import { CourierUpdateComponent } from './courier-update.component';

@Injectable({ providedIn: 'root' })
export class CourierResolve implements Resolve<ICourier> {
  constructor(private service: CourierService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICourier> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((courier: HttpResponse<Courier>) => {
          if (courier.body) {
            return of(courier.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Courier());
  }
}

export const courierRoute: Routes = [
  {
    path: '',
    component: CourierComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'garbageApp.courier.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CourierDetailComponent,
    resolve: {
      courier: CourierResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'garbageApp.courier.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourierUpdateComponent,
    resolve: {
      courier: CourierResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'garbageApp.courier.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CourierUpdateComponent,
    resolve: {
      courier: CourierResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'garbageApp.courier.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
