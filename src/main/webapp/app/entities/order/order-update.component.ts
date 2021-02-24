import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IOrder, Order } from 'app/shared/model/order.model';
import { OrderService } from './order.service';
import { IGarbage } from 'app/shared/model/garbage.model';
import { GarbageService } from 'app/entities/garbage/garbage.service';
import { IAppUser } from 'app/shared/model/app-user.model';
import { AppUserService } from 'app/entities/app-user/app-user.service';
import { ICourier } from 'app/shared/model/courier.model';
import { CourierService } from 'app/entities/courier/courier.service';

type SelectableEntity = IGarbage | IAppUser | ICourier;

@Component({
  selector: 'jhi-order-update',
  templateUrl: './order-update.component.html',
})
export class OrderUpdateComponent implements OnInit {
  isSaving = false;
  garbage: IGarbage[] = [];
  appusers: IAppUser[] = [];
  couriers: ICourier[] = [];

  editForm = this.fb.group({
    id: [],
    orderDate: [],
    price: [],
    finishDate: [],
    userPhotoUrl: [],
    courierPhotoUrl: [],
    endOrderPhotoUrl: [],
    orderStartDate: [],
    orderFinishDate: [],
    orderStatus: [],
    courierRatio: [],
    userRatio: [],
    garbage: [],
    user: [],
    courier: [],
  });

  constructor(
    protected orderService: OrderService,
    protected garbageService: GarbageService,
    protected appUserService: AppUserService,
    protected courierService: CourierService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ order }) => {
      if (!order.id) {
        const today = moment().startOf('day');
        order.orderDate = today;
        order.finishDate = today;
        order.orderStartDate = today;
        order.orderFinishDate = today;
      }

      this.updateForm(order);

      this.garbageService
        .query({ filter: 'order-is-null' })
        .pipe(
          map((res: HttpResponse<IGarbage[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IGarbage[]) => {
          if (!order.garbage || !order.garbage.id) {
            this.garbage = resBody;
          } else {
            this.garbageService
              .find(order.garbage.id)
              .pipe(
                map((subRes: HttpResponse<IGarbage>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IGarbage[]) => (this.garbage = concatRes));
          }
        });

      this.appUserService.query().subscribe((res: HttpResponse<IAppUser[]>) => (this.appusers = res.body || []));

      this.courierService.query().subscribe((res: HttpResponse<ICourier[]>) => (this.couriers = res.body || []));
    });
  }

  updateForm(order: IOrder): void {
    this.editForm.patchValue({
      id: order.id,
      orderDate: order.orderDate ? order.orderDate.format(DATE_TIME_FORMAT) : null,
      price: order.price,
      finishDate: order.finishDate ? order.finishDate.format(DATE_TIME_FORMAT) : null,
      userPhotoUrl: order.userPhotoUrl,
      courierPhotoUrl: order.courierPhotoUrl,
      endOrderPhotoUrl: order.endOrderPhotoUrl,
      orderStartDate: order.orderStartDate ? order.orderStartDate.format(DATE_TIME_FORMAT) : null,
      orderFinishDate: order.orderFinishDate ? order.orderFinishDate.format(DATE_TIME_FORMAT) : null,
      orderStatus: order.orderStatus,
      courierRatio: order.courierRatio,
      userRatio: order.userRatio,
      garbage: order.garbage,
      user: order.user,
      courier: order.courier,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const order = this.createFromForm();
    if (order.id !== undefined) {
      this.subscribeToSaveResponse(this.orderService.update(order));
    } else {
      this.subscribeToSaveResponse(this.orderService.create(order));
    }
  }

  private createFromForm(): IOrder {
    return {
      ...new Order(),
      id: this.editForm.get(['id'])!.value,
      orderDate: this.editForm.get(['orderDate'])!.value ? moment(this.editForm.get(['orderDate'])!.value, DATE_TIME_FORMAT) : undefined,
      price: this.editForm.get(['price'])!.value,
      finishDate: this.editForm.get(['finishDate'])!.value ? moment(this.editForm.get(['finishDate'])!.value, DATE_TIME_FORMAT) : undefined,
      userPhotoUrl: this.editForm.get(['userPhotoUrl'])!.value,
      courierPhotoUrl: this.editForm.get(['courierPhotoUrl'])!.value,
      endOrderPhotoUrl: this.editForm.get(['endOrderPhotoUrl'])!.value,
      orderStartDate: this.editForm.get(['orderStartDate'])!.value
        ? moment(this.editForm.get(['orderStartDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      orderFinishDate: this.editForm.get(['orderFinishDate'])!.value
        ? moment(this.editForm.get(['orderFinishDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      orderStatus: this.editForm.get(['orderStatus'])!.value,
      courierRatio: this.editForm.get(['courierRatio'])!.value,
      userRatio: this.editForm.get(['userRatio'])!.value,
      garbage: this.editForm.get(['garbage'])!.value,
      user: this.editForm.get(['user'])!.value,
      courier: this.editForm.get(['courier'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrder>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
