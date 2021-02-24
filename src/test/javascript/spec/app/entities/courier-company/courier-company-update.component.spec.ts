import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GarbageTestModule } from '../../../test.module';
import { CourierCompanyUpdateComponent } from 'app/entities/courier-company/courier-company-update.component';
import { CourierCompanyService } from 'app/entities/courier-company/courier-company.service';
import { CourierCompany } from 'app/shared/model/courier-company.model';

describe('Component Tests', () => {
  describe('CourierCompany Management Update Component', () => {
    let comp: CourierCompanyUpdateComponent;
    let fixture: ComponentFixture<CourierCompanyUpdateComponent>;
    let service: CourierCompanyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GarbageTestModule],
        declarations: [CourierCompanyUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(CourierCompanyUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourierCompanyUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CourierCompanyService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CourierCompany(123);
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
        const entity = new CourierCompany();
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
