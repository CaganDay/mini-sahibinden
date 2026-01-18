package com.minisahibinden.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.minisahibinden.entity.Car;
import com.minisahibinden.entity.House;
import com.minisahibinden.repository.CarRepository;
import com.minisahibinden.repository.HouseRepository;

@Controller
public class HomeController {

    private final CarRepository carRepository;
    private final HouseRepository houseRepository;
    
    // All 81 cities of Turkey
    private static final List<String> TURKEY_CITIES = Arrays.asList(
        "Adana", "Adiyaman", "Afyonkarahisar", "Agri", "Aksaray", "Amasya", "Ankara", "Antalya", "Ardahan", "Artvin",
        "Aydin", "Balikesir", "Bartin", "Batman", "Bayburt", "Bilecik", "Bingol", "Bitlis", "Bolu", "Burdur",
        "Bursa", "Canakkale", "Cankiri", "Corum", "Denizli", "Diyarbakir", "Duzce", "Edirne", "Elazig", "Erzincan",
        "Erzurum", "Eskisehir", "Gaziantep", "Giresun", "Gumushane", "Hakkari", "Hatay", "Igdir", "Isparta", "Istanbul",
        "Izmir", "Kahramanmaras", "Karabuk", "Karaman", "Kars", "Kastamonu", "Kayseri", "Kilis", "Kirikkale", "Kirklareli",
        "Kirsehir", "Kocaeli", "Konya", "Kutahya", "Malatya", "Manisa", "Mardin", "Mersin", "Mugla", "Mus",
        "Nevsehir", "Nigde", "Ordu", "Osmaniye", "Rize", "Sakarya", "Samsun", "Sanliurfa", "Siirt", "Sinop",
        "Sirnak", "Sivas", "Tekirdag", "Tokat", "Trabzon", "Tunceli", "Usak", "Van", "Yalova", "Yozgat", "Zonguldak"
    );

    public HomeController(CarRepository carRepository, HouseRepository houseRepository) {
        this.carRepository = carRepository;
        this.houseRepository = houseRepository;
    }

    @GetMapping("/")
    public String home(Model model, 
                       @RequestParam(name = "keyword", required = false) String keyword,
                       @RequestParam(name = "year", required = false) Integer year,
                       @RequestParam(name = "city", required = false) String city,
                       @RequestParam(name = "roomCount", required = false) String roomCount) {
        
        // Car filters
        List<Integer> years = carRepository.getDistinctYears();
        List<Car> cars;

        if (keyword != null && !keyword.isEmpty()) {
            cars = carRepository.searchCarsSQL(keyword);
        } else if (year != null) {
            cars = carRepository.filterByYearSQL(year);
        } else {
            cars = carRepository.findAll();
        }

        // House filters
        List<String> cities = TURKEY_CITIES;
        List<String> roomCounts = houseRepository.getDistinctRoomCounts();
        List<House> houses;

        if (city != null && !city.isEmpty()) {
            houses = houseRepository.findByCity(city);
        } else if (roomCount != null && !roomCount.isEmpty()) {
            houses = houseRepository.findByRoomCount(roomCount);
        } else {
            houses = houseRepository.findAll();
        }

        // Determine which tab should be active
        String activeTab = "cars";
        if (city != null || roomCount != null) {
            activeTab = "houses";
        }

        // Add to model
        model.addAttribute("years", years);
        model.addAttribute("cars", cars);
        model.addAttribute("cities", cities);
        model.addAttribute("roomCounts", roomCounts);
        model.addAttribute("houses", houses);
        model.addAttribute("activeTab", activeTab);
        
        return "home";
    }

    @GetMapping("/car/{id}")
    public String carDetail(@PathVariable Long id, Model model) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id:" + id));
        model.addAttribute("car", car);
        return "car-detail";
    }

    @GetMapping("/house/{id}")
    public String houseDetail(@PathVariable Long id, Model model) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid house Id:" + id));
        model.addAttribute("house", house);
        return "house-detail";
    }

    @GetMapping("/post")
    public String showPostAdForm(Model model) {
        model.addAttribute("cities", TURKEY_CITIES);
        return "post-ad";
    }

    @PostMapping("/post/car")
    public String postCar(@RequestParam String model,
                          @RequestParam Integer modelYear,
                          @RequestParam BigDecimal price,
                          @RequestParam Integer kilometers,
                          RedirectAttributes redirectAttributes) {
        Car car = new Car(modelYear, model, price, kilometers);
        carRepository.save(car);
        redirectAttributes.addFlashAttribute("success", "Car ad posted successfully!");
        return "redirect:/post";
    }

    @PostMapping("/post/house")
    public String postHouse(@RequestParam String city,
                            @RequestParam String district,
                            @RequestParam(required = false) String neighborhood,
                            @RequestParam Double squareMeters,
                            @RequestParam String roomCount,
                            @RequestParam BigDecimal price,
                            @RequestParam String sellerType,
                            RedirectAttributes redirectAttributes) {
        House house = new House(sellerType, squareMeters, roomCount, city, district, 
                               neighborhood != null ? neighborhood : "Unknown", LocalDate.now(), price);
        houseRepository.save(house);
        redirectAttributes.addFlashAttribute("success", "House ad posted successfully!");
        return "redirect:/post";
    }
}