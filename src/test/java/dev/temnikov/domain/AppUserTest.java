package dev.temnikov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.temnikov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class AppUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppUser.class);
        AppUser appUser1 = new AppUser();
        appUser1.setId(1L);
        AppUser appUser2 = new AppUser();
        appUser2.setId(appUser1.getId());
        assertThat(appUser1).isEqualTo(appUser2);
        appUser2.setId(2L);
        assertThat(appUser1).isNotEqualTo(appUser2);
        appUser1.setId(null);
        assertThat(appUser1).isNotEqualTo(appUser2);
    }
}
