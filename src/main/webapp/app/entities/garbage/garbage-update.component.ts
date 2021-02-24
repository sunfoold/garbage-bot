import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IGarbage, Garbage } from 'app/shared/model/garbage.model';
import { GarbageService } from './garbage.service';

@Component({
  selector: 'jhi-garbage-update',
  templateUrl: './garbage-update.component.html',
})
export class GarbageUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    pockets: [],
    hugeThings: [],
  });

  constructor(protected garbageService: GarbageService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ garbage }) => {
      this.updateForm(garbage);
    });
  }

  updateForm(garbage: IGarbage): void {
    this.editForm.patchValue({
      id: garbage.id,
      pockets: garbage.pockets,
      hugeThings: garbage.hugeThings,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const garbage = this.createFromForm();
    if (garbage.id !== undefined) {
      this.subscribeToSaveResponse(this.garbageService.update(garbage));
    } else {
      this.subscribeToSaveResponse(this.garbageService.create(garbage));
    }
  }

  private createFromForm(): IGarbage {
    return {
      ...new Garbage(),
      id: this.editForm.get(['id'])!.value,
      pockets: this.editForm.get(['pockets'])!.value,
      hugeThings: this.editForm.get(['hugeThings'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGarbage>>): void {
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
