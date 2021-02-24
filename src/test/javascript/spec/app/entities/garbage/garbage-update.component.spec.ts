import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GarbageTestModule } from '../../../test.module';
import { GarbageUpdateComponent } from 'app/entities/garbage/garbage-update.component';
import { GarbageService } from 'app/entities/garbage/garbage.service';
import { Garbage } from 'app/shared/model/garbage.model';

describe('Component Tests', () => {
  describe('Garbage Management Update Component', () => {
    let comp: GarbageUpdateComponent;
    let fixture: ComponentFixture<GarbageUpdateComponent>;
    let service: GarbageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GarbageTestModule],
        declarations: [GarbageUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(GarbageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GarbageUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GarbageService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Garbage(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Garbage();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
