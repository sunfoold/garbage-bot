import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ICourier, Courier } from 'app/shared/model/courier.model';
import { CourierService } from './courier.service';
import { ICourierCompany } from 'app/shared/model/courier-company.model';
import { CourierCompanyService } from 'app/entities/courier-company/courier-company.service';

@Component({
  selector: 'jhi-courier-update',
  templateUrl: './courier-update.component.html',
})
export class CourierUpdateComponent implements OnInit {
  isSaving = false;
  couriercompanies: ICourierCompany[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    phoneNumber: [],
    photoUrl: [],
    telegramChatId: [],
    joinDate: [],
    balance: [],
    company: [],
  });

  constructor(
    protected courierService: CourierService,
    protected courierCompanyService: CourierCompanyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courier }) => {
      if (!courier.id) {
        const today = moment().startOf('day');
        courier.joinDate = today;
      }

      this.updateForm(courier);

      this.courierCompanyService.query().subscribe((res: HttpResponse<ICourierCompany[]>) => (this.couriercompanies = res.body || []));
    });
  }

  updateForm(courier: ICourier): void {
    this.editForm.patchValue({
      id: courier.id,
      name: courier.name,
      phoneNumber: courier.phoneNumber,
      photoUrl: courier.photoUrl,
      telegramChatId: courier.telegramChatId,
      joinDate: courier.joinDate ? courier.joinDate.format(DATE_TIME_FORMAT) : null,
      balance: courier.balance,
      company: courier.company,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courier = this.createFromForm();
    if (courier.id !== undefined) {
      this.subscribeToSaveResponse(this.courierService.update(courier));
    } else {
      this.subscribeToSaveResponse(this.courierService.create(courier));
    }
  }

  private createFromForm(): ICourier {
    return {
      ...new Courier(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      photoUrl: this.editForm.get(['photoUrl'])!.value,
      telegramChatId: this.editForm.get(['telegramChatId'])!.value,
      joinDate: this.editForm.get(['joinDate'])!.value ? moment(this.editForm.get(['joinDate'])!.value, DATE_TIME_FORMAT) : undefined,
      balance: this.editForm.get(['balance'])!.value,
      company: this.editForm.get(['company'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourier>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ICourierCompany): any {
    return item.id;
  }
}
