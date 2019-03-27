package es.salenda.micronaut.graal;

import com.fasterxml.jackson.databind.util.JSONPObject;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client("http://api.open-notify.org")
public interface BritishNationalBibliographyLowLevelClient {

    @Get("iss-now.json")
    JSONPObject fetchInformation();

}
