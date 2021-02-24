import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IShift } from 'app/shared/model/shift.model';
import { ShiftService } from './shift.service';

@Component({
  templateUrl: './shift-delete-dialog.component.html',
})
export class ShiftDeleteDialogComponent {
  shift?: IShift;

  constructor(protected shiftService: ShiftService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.shiftService.delete(id).subscribe(() => {
      this.eventManager.broadcast('shiftListModification');
      this.activeModal.close();
    });
  }
}
