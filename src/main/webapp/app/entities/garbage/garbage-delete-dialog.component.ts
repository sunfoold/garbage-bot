import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGarbage } from 'app/shared/model/garbage.model';
import { GarbageService } from './garbage.service';

@Component({
  templateUrl: './garbage-delete-dialog.component.html',
})
export class GarbageDeleteDialogComponent {
  garbage?: IGarbage;

  constructor(protected garbageService: GarbageService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.garbageService.delete(id).subscribe(() => {
      this.eventManager.broadcast('garbageListModification');
      this.activeModal.close();
    });
  }
}
