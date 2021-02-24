import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GarbageSharedModule } from 'app/shared/shared.module';
import { CourierCompanyComponent } from './courier-company.component';
import { CourierCompanyDetailComponent } from './courier-company-detail.component';
import { CourierCompanyUpdateComponent } from './courier-company-update.component';
import { CourierCompanyDeleteDialogComponent } from './courier-company-delete-dialog.component';
import { courierCompanyRoute } from './courier-company.route';

@NgModule({
  imports: [GarbageSharedModule, RouterModule.forChild(courierCompanyRoute)],
  declarations: [
    CourierCompanyComponent,
    CourierCompanyDetailComponent,
    CourierCompanyUpdateComponent,
    CourierCompanyDeleteDialogComponent,
  ],
  entryComponents: [CourierCompanyDeleteDialogComponent],
})
export class GarbageCourierCompanyModule {}
