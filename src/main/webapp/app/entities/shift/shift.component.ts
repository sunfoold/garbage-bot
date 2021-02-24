import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IShift } from 'app/shared/model/shift.model';
import { ShiftService } from './shift.service';
import { ShiftDeleteDialogComponent } from './shift-delete-dialog.component';

@Component({
  selector: 'jhi-shift',
  templateUrl: './shift.component.html',
})
export class ShiftComponent implements OnInit, OnDestroy {
  shifts?: IShift[];
  eventSubscriber?: Subscription;

  constructor(protected shiftService: ShiftService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.shiftService.query().subscribe((res: HttpResponse<IShift[]>) => (this.shifts = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInShifts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IShift): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInShifts(): void {
    this.eventSubscriber = this.eventManager.subscribe('shiftListModification', () => this.loadAll());
  }

  delete(shift: IShift): void {
    const modalRef = this.modalService.open(ShiftDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.shift = shift;
  }
}
