import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IShift, Shift } from 'app/shared/model/shift.model';
import { ShiftService } from './shift.service';

@Component({
  selector: 'jhi-shift-update',
  templateUrl: './shift-update.component.html',
})
export class ShiftUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    shiftPlanStartDate: [],
    shiftFactStartDate: [],
    shiftPlanEndDate: [],
    shiftFactEndDate: [],
    prepaid: [],
  });

  constructor(protected shiftService: ShiftService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shift }) => {
      if (!shift.id) {
        const today = moment().startOf('day');
        shift.shiftPlanStartDate = today;
        shift.shiftFactStartDate = today;
        shift.shiftPlanEndDate = today;
        shift.shiftFactEndDate = today;
      }

      this.updateForm(shift);
    });
  }

  updateForm(shift: IShift): void {
    this.editForm.patchValue({
      id: shift.id,
      shiftPlanStartDate: shift.shiftPlanStartDate ? shift.shiftPlanStartDate.format(DATE_TIME_FORMAT) : null,
      shiftFactStartDate: shift.shiftFactStartDate ? shift.shiftFactStartDate.format(DATE_TIME_FORMAT) : null,
      shiftPlanEndDate: shift.shiftPlanEndDate ? shift.shiftPlanEndDate.format(DATE_TIME_FORMAT) : null,
      shiftFactEndDate: shift.shiftFactEndDate ? shift.shiftFactEndDate.format(DATE_TIME_FORMAT) : null,
      prepaid: shift.prepaid,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const shift = this.createFromForm();
    if (shift.id !== undefined) {
      this.subscribeToSaveResponse(this.shiftService.update(shift));
    } else {
      this.subscribeToSaveResponse(this.shiftService.create(shift));
    }
  }

  private createFromForm(): IShift {
    return {
      ...new Shift(),
      id: this.editForm.get(['id'])!.value,
      shiftPlanStartDate: this.editForm.get(['shiftPlanStartDate'])!.value
        ? moment(this.editForm.get(['shiftPlanStartDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      shiftFactStartDate: this.editForm.get(['shiftFactStartDate'])!.value
        ? moment(this.editForm.get(['shiftFactStartDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      shiftPlanEndDate: this.editForm.get(['shiftPlanEndDate'])!.value
        ? moment(this.editForm.get(['shiftPlanEndDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      shiftFactEndDate: this.editForm.get(['shiftFactEndDate'])!.value
        ? moment(this.editForm.get(['shiftFactEndDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      prepaid: this.editForm.get(['prepaid'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShift>>): void {
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
