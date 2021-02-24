import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGarbage } from 'app/shared/model/garbage.model';
import { GarbageService } from './garbage.service';
import { GarbageDeleteDialogComponent } from './garbage-delete-dialog.component';

@Component({
  selector: 'jhi-garbage',
  templateUrl: './garbage.component.html',
})
export class GarbageComponent implements OnInit, OnDestroy {
  garbage?: IGarbage[];
  eventSubscriber?: Subscription;

  constructor(protected garbageService: GarbageService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.garbageService.query().subscribe((res: HttpResponse<IGarbage[]>) => (this.garbage = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInGarbage();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IGarbage): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInGarbage(): void {
    this.eventSubscriber = this.eventManager.subscribe('garbageListModification', () => this.loadAll());
  }

  delete(garbage: IGarbage): void {
    const modalRef = this.modalService.open(GarbageDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.garbage = garbage;
  }
}
