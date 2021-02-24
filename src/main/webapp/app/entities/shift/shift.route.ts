import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IShift, Shift } from 'app/shared/model/shift.model';
import { ShiftService } from './shift.service';
import { ShiftComponent } from './shift.component';
import { ShiftDetailComponent } from './shift-detail.component';
import { ShiftUpdateComponent } from './shift-update.component';

@Injectable({ providedIn: 'root' })
export class ShiftResolve implements Resolve<IShift> {
  constructor(private service: ShiftService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IShift> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((shift: HttpResponse<Shift>) => {
          if (shift.body) {
            return of(shift.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Shift());
  }
}

export const shiftRoute: Routes = [
  {
    path: '',
    component: ShiftComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'garbageApp.shift.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ShiftDetailComponent,
    resolve: {
      shift: ShiftResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'garbageApp.shift.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ShiftUpdateComponent,
    resolve: {
      shift: ShiftResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'garbageApp.shift.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ShiftUpdateComponent,
    resolve: {
      shift: ShiftResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'garbageApp.shift.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
