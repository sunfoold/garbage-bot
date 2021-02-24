import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICourierCompany, CourierCompany } from 'app/shared/model/courier-company.model';
import { CourierCompanyService } from './courier-company.service';

@Component({
  selector: 'jhi-courier-company-update',
  templateUrl: './courier-company-update.component.html',
})
export class CourierCompanyUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    inn: [],
    ogrn: [],
    address: [],
    phone: [],
    isActive: [],
  });

  constructor(protected courierCompanyService: CourierCompanyService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courierCompany }) => {
      this.updateForm(courierCompany);
    });
  }

  updateForm(courierCompany: ICourierCompany): void {
    this.editForm.patchValue({
      id: courierCompany.id,
      name: courierCompany.name,
      inn: courierCompany.inn,
      ogrn: courierCompany.ogrn,
      address: courierCompany.address,
      phone: courierCompany.phone,
      isActive: courierCompany.isActive,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courierCompany = this.createFromForm();
    if (courierCompany.id !== undefined) {
      this.subscribeToSaveResponse(this.courierCompanyService.update(courierCompany));
    } else {
      this.subscribeToSaveResponse(this.courierCompanyService.create(courierCompany));
    }
  }

  private createFromForm(): ICourierCompany {
    return {
      ...new CourierCompany(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      inn: this.editForm.get(['inn'])!.value,
      ogrn: this.editForm.get(['ogrn'])!.value,
      address: this.editForm.get(['address'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourierCompany>>): void {
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
}
