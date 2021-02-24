import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'order',
        loadChildren: () => import('./order/order.module').then(m => m.GarbageOrderModule),
      },
      {
        path: 'payment',
        loadChildren: () => import('./payment/payment.module').then(m => m.GarbagePaymentModule),
      },
      {
        path: 'app-user',
        loadChildren: () => import('./app-user/app-user.module').then(m => m.GarbageAppUserModule),
      },
      {
        path: 'courier',
        loadChildren: () => import('./courier/courier.module').then(m => m.GarbageCourierModule),
      },
      {
        path: 'shift',
        loadChildren: () => import('./shift/shift.module').then(m => m.GarbageShiftModule),
      },
      {
        path: 'courier-company',
        loadChildren: () => import('./courier-company/courier-company.module').then(m => m.GarbageCourierCompanyModule),
      },
      {
        path: 'garbage',
        loadChildren: () => import('./garbage/garbage.module').then(m => m.GarbageGarbageModule),
      },
      {
        path: 'address',
        loadChildren: () => import('./address/address.module').then(m => m.GarbageAddressModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class GarbageEntityModule {}
