import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICourierCompany } from 'app/shared/model/courier-company.model';
import { CourierCompanyService } from './courier-company.service';

@Component({
  templateUrl: './courier-company-delete-dialog.component.html',
})
export class CourierCompanyDeleteDialogComponent {
  courierCompany?: ICourierCompany;

  constructor(
    protected courierCompanyService: CourierCompanyService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.courierCompanyService.delete(id).subscribe(() => {
      this.eventManager.broadcast('courierCompanyListModification');
      this.activeModal.close();
    });
  }
}
