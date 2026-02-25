package org.pedrosaez.proyectocompraentradas.feignclients;


import org.pedrosaez.proyectocompraentradas.purchase.model.response.EventDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "proyectoEventos", url= "http://localhost:8081")
public interface EventFeignClient {

    @GetMapping("/events/{id}")
    EventDTO getEventById(@PathVariable("id") Long id);

    @GetMapping("/events")
    EventDTO getAllEvents();

}
