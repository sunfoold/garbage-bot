package dev.temnikov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.temnikov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class CourierCompanyTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourierCompany.class);
        CourierCompany courierCompany1 = new CourierCompany();
        courierCompany1.setId(1L);
        CourierCompany courierCompany2 = new CourierCompany();
        courierCompany2.setId(courierCompany1.getId());
        assertThat(courierCompany1).isEqualTo(courierCompany2);
        courierCompany2.setId(2L);
        assertThat(courierCompany1).isNotEqualTo(courierCompany2);
        courierCompany1.setId(null);
        assertThat(courierCompany1).isNotEqualTo(courierCompany2);
    }
}
