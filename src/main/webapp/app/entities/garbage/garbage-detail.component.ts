import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGarbage } from 'app/shared/model/garbage.model';

@Component({
  selector: 'jhi-garbage-detail',
  templateUrl: './garbage-detail.component.html',
})
export class GarbageDetailComponent implements OnInit {
  garbage: IGarbage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ garbage }) => (this.garbage = garbage));
  }

  previousState(): void {
    window.history.back();
  }
}
