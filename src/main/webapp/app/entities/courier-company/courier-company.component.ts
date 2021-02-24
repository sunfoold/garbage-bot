import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICourierCompany } from 'app/shared/model/courier-company.model';
import { CourierCompanyService } from './courier-company.service';
import { CourierCompanyDeleteDialogComponent } from './courier-company-delete-dialog.component';

@Component({
  selector: 'jhi-courier-company',
  templateUrl: './courier-company.component.html',
})
export class CourierCompanyComponent implements OnInit, OnDestroy {
  courierCompanies?: ICourierCompany[];
  eventSubscriber?: Subscription;

  constructor(
    protected courierCompanyService: CourierCompanyService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.courierCompanyService.query().subscribe((res: HttpResponse<ICourierCompany[]>) => (this.courierCompanies = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCourierCompanies();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICourierCompany): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCourierCompanies(): void {
    this.eventSubscriber = this.eventManager.subscribe('courierCompanyListModification', () => this.loadAll());
  }

  delete(courierCompany: ICourierCompany): void {
    const modalRef = this.modalService.open(CourierCompanyDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.courierCompany = courierCompany;
  }
}
