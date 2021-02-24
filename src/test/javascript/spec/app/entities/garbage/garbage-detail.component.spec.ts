import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GarbageTestModule } from '../../../test.module';
import { GarbageDetailComponent } from 'app/entities/garbage/garbage-detail.component';
import { Garbage } from 'app/shared/model/garbage.model';

describe('Component Tests', () => {
  describe('Garbage Management Detail Component', () => {
    let comp: GarbageDetailComponent;
    let fixture: ComponentFixture<GarbageDetailComponent>;
    const route = ({ data: of({ garbage: new Garbage(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GarbageTestModule],
        declarations: [GarbageDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(GarbageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GarbageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load garbage on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.garbage).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
