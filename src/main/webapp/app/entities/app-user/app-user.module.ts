import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GarbageSharedModule } from 'app/shared/shared.module';
import { AppUserComponent } from './app-user.component';
import { AppUserDetailComponent } from './app-user-detail.component';
import { AppUserUpdateComponent } from './app-user-update.component';
import { AppUserDeleteDialogComponent } from './app-user-delete-dialog.component';
import { appUserRoute } from './app-user.route';

@NgModule({
  imports: [GarbageSharedModule, RouterModule.forChild(appUserRoute)],
  declarations: [AppUserComponent, AppUserDetailComponent, AppUserUpdateComponent, AppUserDeleteDialogComponent],
  entryComponents: [AppUserDeleteDialogComponent],
})
export class GarbageAppUserModule {}
