package com.graphhopper.navigation;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.GraphHopperConfig;
import com.graphhopper.util.TranslationMap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NavigateResourceTest {

    private GraphHopper graphHopper;
    private TranslationMap translationMap;
    private NavigateResource resource;

    @BeforeEach
    void setUp() {
        graphHopper = mock(GraphHopper.class);
        translationMap = mock(TranslationMap.class);
        GraphHopperConfig config = new GraphHopperConfig();
        resource = new NavigateResource(graphHopper, translationMap, config);
    }

    @Test
    public void voiceInstructionsTest() {

        List<Double> bearings = NavigateResource.getBearing("");
        assertEquals(0, bearings.size());
        assertEquals(Collections.EMPTY_LIST, bearings);

        bearings = NavigateResource.getBearing("100,1");
        assertEquals(1, bearings.size());
        assertEquals(100, bearings.get(0), .1);

        bearings = NavigateResource.getBearing(";100,1;;");
        assertEquals(4, bearings.size());
        assertEquals(100, bearings.get(1), .1);
    }

    @Test
    void TestGetBearingErreurNonNumeric() {
        assertThrows(IllegalArgumentException.class, () -> NavigateResource.getBearing("abc,1"));
    }

    @Test
    void TestGetBearingParseWithNaN() {
        var b = NavigateResource.getBearing("100,1;;200,1;");
        assertEquals(4, b.size());
        assertEquals(100d, b.get(0), 0.1);
        assertTrue(Double.isNaN(b.get(1)));
        assertEquals(200d, b.get(2), 0.1);
        assertTrue(Double.isNaN(b.get(3)));
    }

    @Test
    void TestDoGetStepsDesactive() {
        NavigateResource res = new NavigateResource(null, new TranslationMap(), new GraphHopperConfig());

        assertThrows(IllegalArgumentException.class, () ->
                res.doGet(
                        null, null, null,
                        false,               // steps désactivés -> doit lancer IllegalArgumentException
                        true,
                        true,
                        true,
                        "metric",
                        "simplified",
                        "polyline6",
                        "",
                        "en",
                        "driving"
                )
        );
    }


    @Test
    void getBearingParsesValidBearingStringWithNaN() {
        // given
        String bearingString = "10,90;20,180;";

        // when
        List<Double> result = NavigateResource.getBearing(bearingString);

        // then
        assertEquals(3, result.size());
        assertEquals(10.0, result.get(0));
        assertEquals(20.0, result.get(1));
        assertTrue(Double.isNaN(result.get(2)));
    }
}
