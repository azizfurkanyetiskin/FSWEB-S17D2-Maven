package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/developers")
public class DeveloperController {
    private Map<Integer, Developer> developers;
    private Taxable taxable;

    @Autowired
    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }

    @PostConstruct
    public void init() {
        developers = new HashMap<>();
    }

    @GetMapping
    public List<Developer> getAllDevelopers() {
        return developers.values().stream().collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Developer getDeveloperById(@PathVariable int id) {
        return developers.get(id);
    }

    @PostMapping
    public Developer addDeveloper(@RequestBody Developer developer) {
        Developer newDev = null;
        if (developer.getExperience() == Experience.JUNIOR) {
            double netSalary = developer.getSalary() - (developer.getSalary() * taxable.getSimpleTaxRate() / 100);
            newDev = new JuniorDeveloper(developer.getId(), developer.getName(), netSalary);
        } else if (developer.getExperience() == Experience.MID) {
            double netSalary = developer.getSalary() - (developer.getSalary() * taxable.getMiddleTaxRate() / 100);
            newDev = new MidDeveloper(developer.getId(), developer.getName(), netSalary);
        } else if (developer.getExperience() == Experience.SENIOR) {
            double netSalary = developer.getSalary() - (developer.getSalary() * taxable.getUpperTaxRate() / 100);
            newDev = new SeniorDeveloper(developer.getId(), developer.getName(), netSalary);
        } else {
            newDev = developer;
        }
        developers.put(newDev.getId(), newDev);
        return newDev;
    }

    @PutMapping("/{id}")
    public Developer updateDeveloper(@PathVariable int id, @RequestBody Developer developer) {
        if (developers.containsKey(id)) {
            Developer existing = developers.get(id);
            existing.setName(developer.getName());
            existing.setSalary(developer.getSalary());
            existing.setExperience(developer.getExperience());
            developers.put(id, existing);
            return existing;
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public Developer deleteDeveloper(@PathVariable int id) {
        return developers.remove(id);
    }
}