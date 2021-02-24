import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GarbageTestModule } from '../../../test.module';
import { GarbageComponent } from 'app/entities/garbage/garbage.component';
import { GarbageService } from 'app/entities/garbage/garbage.service';
import { Garbage } from 'app/shared/model/garbage.model';

describe('Component Tests', () => {
  describe('Garbage Management Component', () => {
    let comp: GarbageComponent;
    let fixture: ComponentFixture<GarbageComponent>;
    let service: GarbageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GarbageTestModule],
        declarations: [GarbageComponent],
      })
        .overrideTemplate(GarbageComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GarbageComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GarbageService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Garbage(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.garbage && comp.garbage[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
