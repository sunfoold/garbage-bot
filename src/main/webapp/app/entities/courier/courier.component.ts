import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICourier } from 'app/shared/model/courier.model';
import { CourierService } from './courier.service';
import { CourierDeleteDialogComponent } from './courier-delete-dialog.component';

@Component({
  selector: 'jhi-courier',
  templateUrl: './courier.component.html',
})
export class CourierComponent implements OnInit, OnDestroy {
  couriers?: ICourier[];
  eventSubscriber?: Subscription;

  constructor(protected courierService: CourierService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.courierService.query().subscribe((res: HttpResponse<ICourier[]>) => (this.couriers = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCouriers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICourier): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCouriers(): void {
    this.eventSubscriber = this.eventManager.subscribe('courierListModification', () => this.loadAll());
  }

  delete(courier: ICourier): void {
    const modalRef = this.modalService.open(CourierDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.courier = courier;
  }
}
