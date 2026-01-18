package com.minisahibinden.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.minisahibinden.entity.Listing;
import com.minisahibinden.entity.RealEstate;
import com.minisahibinden.entity.User;
import com.minisahibinden.entity.Vehicle;
import com.minisahibinden.repository.ListingRepository;
import com.minisahibinden.repository.RealEstateRepository;
import com.minisahibinden.repository.UserRepository;
import com.minisahibinden.repository.VehicleRepository;

@Controller
public class HomeController {

    private static final int PAGE_SIZE = 12; // Show 12 items per page for better performance

    private final VehicleRepository vehicleRepository;
    private final RealEstateRepository realEstateRepository;
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

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

    public HomeController(VehicleRepository vehicleRepository, RealEstateRepository realEstateRepository,
                         ListingRepository listingRepository, UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.realEstateRepository = realEstateRepository;
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(Model model,
                       @RequestParam(name = "keyword", required = false) String keyword,
                       @RequestParam(name = "year", required = false) Integer year,
                       @RequestParam(name = "city", required = false) String city,
                       @RequestParam(name = "roomConfig", required = false) String roomConfig,
                       @RequestParam(name = "vehiclePage", defaultValue = "0") int vehiclePage,
                       @RequestParam(name = "realestatePage", defaultValue = "0") int realestatePage,
                       @RequestParam(name = "tab", required = false) String tab) {

        Pageable vehiclePageable = PageRequest.of(vehiclePage, PAGE_SIZE);
        Pageable realestatePageable = PageRequest.of(realestatePage, PAGE_SIZE);

        // Vehicle filters
        List<Integer> years = vehicleRepository.getDistinctYears();
        Page<Vehicle> vehiclesPage;

        if (keyword != null && !keyword.isEmpty() && !"realestate".equals(tab)) {
            vehiclesPage = vehicleRepository.searchByModelNamePaged(keyword, vehiclePageable);
        } else if (year != null) {
            vehiclesPage = vehicleRepository.filterByYearPaged(year, vehiclePageable);
        } else {
            vehiclesPage = vehicleRepository.findAllActivePaged(vehiclePageable);
        }

        // RealEstate filters
        List<String> cities = TURKEY_CITIES;
        List<String> roomConfigs = realEstateRepository.getDistinctRoomConfigs();
        Page<RealEstate> realEstatesPage;

        if (keyword != null && !keyword.isEmpty() && "realestate".equals(tab)) {
            realEstatesPage = realEstateRepository.searchByLocationPaged(keyword, realestatePageable);
        } else if (city != null && !city.isEmpty()) {
            realEstatesPage = realEstateRepository.findByCityPaged(city, realestatePageable);
        } else if (roomConfig != null && !roomConfig.isEmpty()) {
            realEstatesPage = realEstateRepository.findByRoomConfigPaged(roomConfig, realestatePageable);
        } else {
            realEstatesPage = realEstateRepository.findAllActivePaged(realestatePageable);
        }

        // Determine active tab
        String activeTab = "vehicles";
        if ("realestate".equals(tab) || city != null || roomConfig != null) {
            activeTab = "realestate";
        }

        // Add to model
        model.addAttribute("years", years);
        model.addAttribute("vehicles", vehiclesPage.getContent());
        model.addAttribute("vehiclesPage", vehiclesPage);
        model.addAttribute("cities", cities);
        model.addAttribute("roomConfigs", roomConfigs);
        model.addAttribute("realEstates", realEstatesPage.getContent());
        model.addAttribute("realEstatesPage", realEstatesPage);
        model.addAttribute("activeTab", activeTab);

        return "home";
    }

    @GetMapping("/vehicle/{id}")
    public String vehicleDetail(@PathVariable Integer id, Model model) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));
        model.addAttribute("vehicle", vehicle);
        return "vehicle-detail";
    }

    @GetMapping("/realestate/{id}")
    public String realEstateDetail(@PathVariable Integer id, Model model) {
        RealEstate realEstate = realEstateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid real estate Id:" + id));
        model.addAttribute("realEstate", realEstate);
        return "realestate-detail";
    }

    @GetMapping("/post")
    public String showPostAdForm(Model model) {
        model.addAttribute("cities", TURKEY_CITIES);
        return "post-ad";
    }

    @PostMapping("/post/vehicle")
    public String postVehicle(@RequestParam String modelName,
                              @RequestParam Integer modelYear,
                              @RequestParam BigDecimal price,
                              @RequestParam Integer kilometers,
                              RedirectAttributes redirectAttributes) {
        // Get or create a default user
        User user = userRepository.findById(1)
                .orElseGet(() -> {
                    User newUser = new User("Default User", "default@example.com", "555-0000");
                    return userRepository.save(newUser);
                });

        // Create listing
        Listing listing = new Listing(user, price, LocalDate.now(), Listing.Category.Vehicle);
        listing = listingRepository.save(listing);

        // Create vehicle
        Vehicle vehicle = new Vehicle(listing, modelYear, modelName, kilometers);
        vehicleRepository.save(vehicle);

        redirectAttributes.addFlashAttribute("success", "Vehicle ad posted successfully!");
        return "redirect:/post";
    }

    @PostMapping("/post/realestate")
    public String postRealEstate(@RequestParam String city,
                                 @RequestParam String district,
                                 @RequestParam(required = false) String neighborhood,
                                 @RequestParam Integer areaSqm,
                                 @RequestParam String roomConfig,
                                 @RequestParam BigDecimal price,
                                 @RequestParam String sellerType,
                                 RedirectAttributes redirectAttributes) {
        // Get or create a default user
        User user = userRepository.findById(1)
                .orElseGet(() -> {
                    User newUser = new User("Default User", "default@example.com", "555-0000");
                    return userRepository.save(newUser);
                });

        // Create listing
        Listing listing = new Listing(user, price, LocalDate.now(), Listing.Category.RealEstate);
        listing = listingRepository.save(listing);

        // Create real estate
        RealEstate realEstate = new RealEstate(listing, sellerType, areaSqm, roomConfig,
                city, district, neighborhood != null ? neighborhood : "Unknown");
        realEstateRepository.save(realEstate);

        redirectAttributes.addFlashAttribute("success", "Real estate ad posted successfully!");
        return "redirect:/post";
    }
}
