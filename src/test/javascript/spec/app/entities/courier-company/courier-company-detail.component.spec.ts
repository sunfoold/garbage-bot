import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GarbageTestModule } from '../../../test.module';
import { CourierCompanyDetailComponent } from 'app/entities/courier-company/courier-company-detail.component';
import { CourierCompany } from 'app/shared/model/courier-company.model';

describe('Component Tests', () => {
  describe('CourierCompany Management Detail Component', () => {
    let comp: CourierCompanyDetailComponent;
    let fixture: ComponentFixture<CourierCompanyDetailComponent>;
    const route = ({ data: of({ courierCompany: new CourierCompany(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GarbageTestModule],
        declarations: [CourierCompanyDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CourierCompanyDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourierCompanyDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load courierCompany on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.courierCompany).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
