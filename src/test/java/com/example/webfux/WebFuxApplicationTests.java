package com.example.webfux;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
class WebFuxApplicationTests {

    @Test
    public void testReactor() throws Exception{
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5, 6);
        Mono<Integer> mono = Mono.just(1);

        Integer[] arr = {1,2,3,4,5,6};
        Flux<Integer> flux1 = Flux.fromArray(arr);

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        Flux<Integer> flux2 = Flux.fromIterable(list);

        Flux<Integer> flux3 = Flux.from(flux);

        Flux<Integer> flux4 = Flux.fromStream(Stream.of(1, 2, 3, 4, 5, 6));

        flux.subscribe();

        flux1.subscribe(System.out::println);

        flux2.subscribe(System.out::println,System.err::println);

        flux3.subscribe(System.out::println,System.err::println,() -> System.out.println("complete"));

        flux4.subscribe(System.out::println,System.err::println,
                () -> System.out.println("complete"),
                subscription -> subscription.request(3));

        flux.map(i -> {
            System.out.println(Thread.currentThread().getName()+"-map1");
            return i;
        }).publishOn(Schedulers.elastic()).map(
                i -> {
                    System.out.println(Thread.currentThread().getName()+"-map2");
                    return i;
                }
        ).subscribeOn(Schedulers.parallel())
                .subscribe(i -> System.out.println(Thread.currentThread().getName()+"-" + i));

        Thread.sleep(10000);
    }


}
