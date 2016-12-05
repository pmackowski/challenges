package pl.pmackowski.directbus.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.pmackowski.directbus.api.DirectBusStationService;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.pmackowski.directbus.rest.DirectBusController.*;

/**
 * Created by pmackowski on 2016-12-03.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(DirectBusController.class)
public class DirectBusResponseControllerTest {

    private static final int BAD_REQUEST = HttpStatus.BAD_REQUEST.value();

    @Autowired
    private MockMvc mvc;

    @MockBean
    @SuppressWarnings("unused")
    private DirectBusStationService directBusStationService;

    @Test
    public void shouldReturnDirectRoute() throws Exception {
        given(this.directBusStationService.isDirectRoute(1, 2)).willReturn(true);

        this.mvc.perform(get(API_DIRECT).param(DEPARTURE_SID_PARAM, "1").param(ARRIVAL_SID_PARAM, "2"))

                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.dep_sid").value(1))
                .andExpect(jsonPath("$.arr_sid").value(2))
                .andExpect(jsonPath("$.direct_bus_route").value(true));
    }

    @Test
    public void shouldReturnNotDirectRoute() throws Exception {
        given(this.directBusStationService.isDirectRoute(1, 2)).willReturn(false);

        this.mvc.perform(get(API_DIRECT).param(DEPARTURE_SID_PARAM, "1").param(ARRIVAL_SID_PARAM, "2"))

                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.dep_sid").value(1))
                .andExpect(jsonPath("$.arr_sid").value(2))
                .andExpect(jsonPath("$.direct_bus_route").value(false));
    }

    @Test
    public void shouldVerifyIfDepartureSidIsPresent() throws Exception {
        this.mvc.perform(get(API_DIRECT).param(ARRIVAL_SID_PARAM, "2"))
                .andExpect(status().is(BAD_REQUEST))
                .andExpect(status().reason("Required int parameter 'dep_sid' is not present"));
    }

    @Test
    public void shouldVerifyIfArrivalSidIsPresent() throws Exception {
        this.mvc.perform(get(API_DIRECT).param(DEPARTURE_SID_PARAM, "2"))
                .andExpect(status().is(BAD_REQUEST))
                .andExpect(status().reason("Required int parameter 'arr_sid' is not present"));
    }

    @Test
    public void shouldVerifyBadRequestParameter() throws Exception {
        this.mvc.perform(get(API_DIRECT).param(DEPARTURE_SID_PARAM, "a").param(ARRIVAL_SID_PARAM, "b"))
                .andExpect(status().is(BAD_REQUEST));
    }
}