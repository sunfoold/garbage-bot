import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICourier } from 'app/shared/model/courier.model';
import { CourierService } from './courier.service';

@Component({
  templateUrl: './courier-delete-dialog.component.html',
})
export class CourierDeleteDialogComponent {
  courier?: ICourier;

  constructor(protected courierService: CourierService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.courierService.delete(id).subscribe(() => {
      this.eventManager.broadcast('courierListModification');
      this.activeModal.close();
    });
  }
}
