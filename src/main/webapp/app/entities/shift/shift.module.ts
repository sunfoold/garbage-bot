import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GarbageSharedModule } from 'app/shared/shared.module';
import { ShiftComponent } from './shift.component';
import { ShiftDetailComponent } from './shift-detail.component';
import { ShiftUpdateComponent } from './shift-update.component';
import { ShiftDeleteDialogComponent } from './shift-delete-dialog.component';
import { shiftRoute } from './shift.route';

@NgModule({
  imports: [GarbageSharedModule, RouterModule.forChild(shiftRoute)],
  declarations: [ShiftComponent, ShiftDetailComponent, ShiftUpdateComponent, ShiftDeleteDialogComponent],
  entryComponents: [ShiftDeleteDialogComponent],
})
export class GarbageShiftModule {}
