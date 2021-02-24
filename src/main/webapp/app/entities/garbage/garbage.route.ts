import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IGarbage, Garbage } from 'app/shared/model/garbage.model';
import { GarbageService } from './garbage.service';
import { GarbageComponent } from './garbage.component';
import { GarbageDetailComponent } from './garbage-detail.component';
import { GarbageUpdateComponent } from './garbage-update.component';

@Injectable({ providedIn: 'root' })
export class GarbageResolve implements Resolve<IGarbage> {
  constructor(private service: GarbageService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGarbage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((garbage: HttpResponse<Garbage>) => {
          if (garbage.body) {
            return of(garbage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Garbage());
  }
}

export const garbageRoute: Routes = [
  {
    path: '',
    component: GarbageComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'garbageApp.garbage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GarbageDetailComponent,
    resolve: {
      garbage: GarbageResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'garbageApp.garbage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GarbageUpdateComponent,
    resolve: {
      garbage: GarbageResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'garbageApp.garbage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GarbageUpdateComponent,
    resolve: {
      garbage: GarbageResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'garbageApp.garbage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
