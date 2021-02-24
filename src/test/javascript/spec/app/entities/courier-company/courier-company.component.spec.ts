import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GarbageTestModule } from '../../../test.module';
import { CourierCompanyComponent } from 'app/entities/courier-company/courier-company.component';
import { CourierCompanyService } from 'app/entities/courier-company/courier-company.service';
import { CourierCompany } from 'app/shared/model/courier-company.model';

describe('Component Tests', () => {
  describe('CourierCompany Management Component', () => {
    let comp: CourierCompanyComponent;
    let fixture: ComponentFixture<CourierCompanyComponent>;
    let service: CourierCompanyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GarbageTestModule],
        declarations: [CourierCompanyComponent],
      })
        .overrideTemplate(CourierCompanyComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourierCompanyComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CourierCompanyService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CourierCompany(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.courierCompanies && comp.courierCompanies[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
