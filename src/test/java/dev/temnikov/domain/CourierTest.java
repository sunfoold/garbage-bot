package dev.temnikov.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import dev.temnikov.web.rest.TestUtil;

public class CourierTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Courier.class);
        Courier courier1 = new Courier();
        courier1.setId(1L);
        Courier courier2 = new Courier();
        courier2.setId(courier1.getId());
        assertThat(courier1).isEqualTo(courier2);
        courier2.setId(2L);
        assertThat(courier1).isNotEqualTo(courier2);
        courier1.setId(null);
        assertThat(courier1).isNotEqualTo(courier2);
    }
}
