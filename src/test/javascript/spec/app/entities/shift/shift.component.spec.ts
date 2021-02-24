import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GarbageTestModule } from '../../../test.module';
import { ShiftComponent } from 'app/entities/shift/shift.component';
import { ShiftService } from 'app/entities/shift/shift.service';
import { Shift } from 'app/shared/model/shift.model';

describe('Component Tests', () => {
  describe('Shift Management Component', () => {
    let comp: ShiftComponent;
    let fixture: ComponentFixture<ShiftComponent>;
    let service: ShiftService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GarbageTestModule],
        declarations: [ShiftComponent],
      })
        .overrideTemplate(ShiftComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ShiftComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ShiftService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Shift(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.shifts && comp.shifts[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
