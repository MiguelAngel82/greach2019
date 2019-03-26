package es.salenda.micronaut.graal;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Flowable;

import java.util.List;

@Client(BintrayConfiguration.BINTRAY_API_URL)
public interface BintrayClient {

    @Get("/api/${bintray.apiversion}/repos/${bintray.organization}/${bintray.repository}/packages")
    Flowable<BintrayPackage> fetchPackages();

    @Get("/api/${bintray.apiversion}/repos/${bintray.organization}/${bintray.repository}/packages")
    List<BintrayPackage> fetchPackagesNotReactive();
}
