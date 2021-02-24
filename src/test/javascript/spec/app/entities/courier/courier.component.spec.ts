import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GarbageTestModule } from '../../../test.module';
import { CourierComponent } from 'app/entities/courier/courier.component';
import { CourierService } from 'app/entities/courier/courier.service';
import { Courier } from 'app/shared/model/courier.model';

describe('Component Tests', () => {
  describe('Courier Management Component', () => {
    let comp: CourierComponent;
    let fixture: ComponentFixture<CourierComponent>;
    let service: CourierService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GarbageTestModule],
        declarations: [CourierComponent],
      })
        .overrideTemplate(CourierComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourierComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CourierService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Courier(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.couriers && comp.couriers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
